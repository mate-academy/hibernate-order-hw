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

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Transaction transaction = null;
        Session session = null;
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
            throw new DataProcessingException("Can't insert order" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Order> orders = session.createQuery("FROM Order AS o "
                    + "LEFT JOIN FETCH o.tickets AS t "
                    + "LEFT JOIN FETCH o.user AS u "
                    + "LEFT JOIN FETCH t.cinemaHall AS ch "
                    + "LEFT JOIN FETCH t.movie AS m "
                    + "WHERE o.user =: user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
            for (Order currentOrder : orders) {
                currentOrder.setUser(user);
            }
            return orders;
        } catch (Exception e) {
            throw new DataProcessingException("Can't find order history by user " + user, e);
        }
    }
}
