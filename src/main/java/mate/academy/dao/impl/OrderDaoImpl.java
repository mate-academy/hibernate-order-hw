package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {

    @Override
    public List<Order> getByUser(User user) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
           return session.createQuery(
                   "FROM Order o LEFT JOIN FETCH o.tickets WHERE o.user.id = :id", Order.class)
                   .setParameter("id", user.getId()).list();
        }
    }

    @Override
    public Order addOrder(Order order) {
        Session session = null;
        Transaction transaction = null;
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
            throw new RuntimeException("Couldn't add an order", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
