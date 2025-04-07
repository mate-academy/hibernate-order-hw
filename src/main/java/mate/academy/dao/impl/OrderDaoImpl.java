package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class OrderDaoImpl implements OrderDao {
    @Override
    public List<Order> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Order", Order.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can not get history of orders", e);
        }
    }

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User managedUser = session.merge(order.getUser());
            order.setUser(managedUser);
            List<Ticket> managedTickets = session.merge(order.getTickets());
            order.setTickets(managedTickets);
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not add order to the db", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
