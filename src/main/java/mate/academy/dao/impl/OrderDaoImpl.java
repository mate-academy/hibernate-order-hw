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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Cannot complete order by order" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            return session.createQuery("from Order o LEFT JOIN FETCH o.tickets t"
                            + " LEFT JOIN FETCH t.movieSession m "
                            + "LEFT JOIN FETCH m.movie mm "
                            + "LEFT JOIN FETCH m.cinemaHall "
                            + "LEFT JOIN FETCH o.user u ",Order.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get orders history by user: " + user, e);
        }
    }
}
