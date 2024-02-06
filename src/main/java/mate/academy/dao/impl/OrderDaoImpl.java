package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {
    private static final SessionFactory SESSION_FACTORY =
            HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SESSION_FACTORY.openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't inject an order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        String query = """
                FROM Order order
                LEFT JOIN FETCH order.tickets
                LEFT JOIN FETCH order.user
                WHERE order.id = :value
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery(query, Order.class)
                    .setParameter("value", id)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get an order by id " + id, e);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = """
                FROM Order order
                LEFT JOIN FETCH order.user
                LEFT JOIN FETCH order.tickets
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery(query, Order.class).list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all orders from db", e);
        }
    }

    @Override
    public Optional<Order> getByUser(User user) {
        String query = """
                FROM Order order
                LEFT JOIN FETCH order.tickets
                LEFT JOIN FETCH order.user
                WHERE order.user = :value
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery(
                    query, Order.class)
                    .setParameter("value", user)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get an order by user " + user, e);
        }
    }

    @Override
    public List<Order> getAllByUser(User user) {
        String query = """
                FROM Order order
                LEFT JOIN FETCH order.tickets
                LEFT JOIN FETCH order.user
                WHERE order.user = :value
                """;
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery(query, Order.class)
                    .setParameter("value", user)
                    .list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all orders for user " + user, e);
        }
    }
}
