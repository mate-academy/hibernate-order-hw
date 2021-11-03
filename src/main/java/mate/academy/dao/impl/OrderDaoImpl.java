package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class OrderDaoImpl implements OrderDao {
    @Override
    public Order completeOrder(Order order) {
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
            throw new DataProcessingException("Can't complete order: " + order.toString(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderByUser = session.createQuery("FROM Order o "
                    + "JOIN FETCH o.tickets t "
                    + "JOIN FETCH o.user "
                    + "JOIN FETCH t.movieSession ms "
                    + "JOIN FETCH ms.movie "
                    + "JOIN FETCH ms.cinemaHall "
                    + "JOIN FETCH t.user "
                    + "WHERE t.user =:user", Order.class);
            getOrderByUser.setParameter("user", user);
            return getOrderByUser.list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders by user: " + user, e);
        }
    }
}
