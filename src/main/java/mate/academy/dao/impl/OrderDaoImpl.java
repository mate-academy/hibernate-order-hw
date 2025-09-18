package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Cannot save order", exception);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Order getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("from Order where user =: user", Order.class);
            query.setParameter("user", user);
            return query.uniqueResult();
        }
    }
}
