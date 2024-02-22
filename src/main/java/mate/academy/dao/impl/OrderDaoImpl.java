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
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save order '"
                    + order + "' to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> orderQuery = session.createQuery("FROM orders o "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH o.user u "
                    + "WHERE o.user =:user", Order.class);
            orderQuery.setParameter("user", user);
            return orderQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order by user '"
                    + user + "' from DB", e);
        }
    }
}
