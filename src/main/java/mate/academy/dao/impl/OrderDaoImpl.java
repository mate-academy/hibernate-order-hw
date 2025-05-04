package mate.academy.dao.impl;

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
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add order " + order + " to the DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> findByUser(User user) {
        String query = "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH t.movieSession m "
                + "LEFT JOIN FETCH t.user "
                + "LEFT JOIN FETCH m.cinemaHall "
                + "LEFT JOIN FETCH m.movie "
                + "WHERE o.user = :user";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> orderQuery = session.createQuery(query, Order.class);
            orderQuery.setParameter("user", user);
            return orderQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders for user "
                    + user + "by user id", e);
        }
    }
}
