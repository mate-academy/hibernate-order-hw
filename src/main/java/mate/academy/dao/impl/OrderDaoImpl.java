package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
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
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> getByUser(User user) {
        String query = "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH o.orderDate od "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE o.user = :user "
                + "ORDER BY od DESC";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderByUser = session.createQuery(query, Order.class);
            getOrderByUser.setParameter("user", user);
            return Optional.ofNullable(getOrderByUser.uniqueResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't get Order by User " + user, e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        String query = "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH o.orderDate od "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE o.user = :user ";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderByUser = session.createQuery(query, Order.class);
            getOrderByUser.setParameter("user", user);
            return getOrderByUser.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get Order by User " + user, e);
        }
    }
}
