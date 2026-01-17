package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save order: "
                    + order + " to the database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<List<Order>> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = criteriaBuilder
                    .createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            Fetch<Object, Object> ticketsRoot = root
                    .fetch("tickets", JoinType.LEFT);
            Fetch<Object, Object> userRoot = root.fetch("user", JoinType.LEFT);
            Fetch<Object, Object> movieSessionRoot = ticketsRoot
                    .fetch("movieSession", JoinType.LEFT);
            movieSessionRoot.fetch("movie",JoinType.LEFT);
            movieSessionRoot.fetch("cinemaHall", JoinType.LEFT);
            query.select(root).distinct(true).where(criteriaBuilder
                    .equal(root.get("user"), user));
            return Optional.ofNullable(session.createQuery(query)
                    .getResultList());
        } catch (Exception e) {
            throw new DataProcessingException("Can not get a list of orders of user: "
                                                + user, e);
        }
    }
}
