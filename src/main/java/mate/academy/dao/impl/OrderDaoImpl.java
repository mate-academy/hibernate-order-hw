package mate.academy.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    private static final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();

    @Override
    public Order add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        Order order = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            List<Ticket> tickets = session.merge(shoppingCart.getTickets());
            User user = session.merge(shoppingCart.getUser());
            order = createNewOrder(tickets, user);
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.tickets "
                    + "LEFT JOIN FETCH o.user "
                    + "WHERE o.user = :user;", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders by user: " + user, e);
        }
    }

    private Order createNewOrder(List<Ticket> tickets, User user) {
        Order order = new Order();
        order.setTickets(tickets);
        order.setUser(user);
        order.setOrderDate(CURRENT_DATE_TIME);
        return order;
    }
}
