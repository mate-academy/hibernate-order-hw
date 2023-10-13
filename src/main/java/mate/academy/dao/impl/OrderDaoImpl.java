package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

@Dao
public class OrderDaoImpl implements OrderDao {
    private SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cannot add an order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = factory.openSession()) {
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            root.fetch("user", JoinType.INNER);
            Fetch<Object, Object> ticketsFetch = root.fetch("tickets", JoinType.INNER);
            ticketsFetch.fetch("user", JoinType.INNER);
            Fetch<Object, Object> movieSessionfetch =
                    ticketsFetch.fetch("movieSession", JoinType.INNER);
            movieSessionfetch.fetch("cinemaHall", JoinType.INNER);
            movieSessionfetch.fetch("movie", JoinType.INNER);
            query.where(criteriaBuilder.equal(root.get("user"), user));
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find orders of user " + user, e);
        }
    }
}
