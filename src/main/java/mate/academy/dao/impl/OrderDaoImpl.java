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
            throw new DataProcessingException("Cannot add order. order=" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Order o "
                            + "LEFT JOIN FETCH o.tickets t "
                            + "LEFT JOIN FETCH t.movieSession "
                            + "LEFT JOIN FETCH t.user "
                            + "LEFT JOIN FETCH o.user u "
                            + "WHERE u.id = :user_id", Order.class)
                    .setParameter("user_id", user.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get by user. user=" + user, e);
        }
    }
}
