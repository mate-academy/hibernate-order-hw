package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't create order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public Order getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH t.movieSession ms "
                    + "LEFT JOIN FETCH ms.movie "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "WHERE o.user =:user", Order.class);
            query.setParameter("user", user);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find a order by user: " + user, e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = criteriaQuery.from(Order.class);

            root.fetch("tickets", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession", JoinType.LEFT);
            root.fetch("tickets").fetch("user", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession").fetch("movie", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession").fetch("cinemaHall", JoinType.LEFT);

            Predicate orderPredicate = criteriaBuilder.equal(root.get("user").get("id"),
                    user.getId());

            criteriaQuery.select(root).where(orderPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get available orders for user: " + user.getId() + " for date: ", e);
        }
    }
}
