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
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderByUserQuery = session.createQuery("FROM Order o "
                    + "JOIN FETCH o.tickets t "
                    + "JOIN FETCH o.user "
                    + "JOIN FETCH t.movieSession m "
                    + "JOIN FETCH m.movie "
                    + "JOIN FETCH m.cinemaHall "
                    + "WHERE o.user = :user", Order.class);
            getOrderByUserQuery.setParameter("user", user);
            return getOrderByUserQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get list of orders of user: " + user, e);
        }
    }
}
