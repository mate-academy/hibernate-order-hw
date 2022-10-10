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
            throw new DataProcessingException("Can't save order to DB\norder: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT o FROM Order o INNER JOIN FETCH o.tickets t "
                    + "INNER JOIN FETCH o.user "
                    + "INNER JOIN FETCH t.movieSession ms "
                    + "INNER JOIN FETCH ms.movie "
                    + "INNER JOIN FETCH ms.cinemaHall "
                    + "WHERE o.user.id =: userId", Order.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order history\nuser: " + user, e);
        }
    }
}
