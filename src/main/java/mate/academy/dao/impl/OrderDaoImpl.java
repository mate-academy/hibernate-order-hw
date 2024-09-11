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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Failed to add order = [" + order + "]", e);
        } finally {
            session.close();
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Order o "
                            + "join fetch o.tickets "
                            + "join fetch o.user "
                            + "where o.user = :user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new DataProcessingException("Failed to get orders of user = [" + user + "]", e);
        }
    }
}
