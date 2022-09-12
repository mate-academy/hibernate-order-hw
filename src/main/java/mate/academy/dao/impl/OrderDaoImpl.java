package mate.academy.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Optional<Order> completeOrder(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Order order = new Order();
            order.setTickets(shoppingCart.getTickets());
            order.setUser(shoppingCart.getUser());
            order.setOrderDate(LocalDateTime.now());
            session.save(order);
            transaction.commit();
            return Optional.of(order);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t complete order by shopping cart "
                    + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        String query = "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH o.user u "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE o.user = :user ";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getAllOrders = session.createQuery(query, Order.class);
            getAllOrders.setParameter("user", user);
            return getAllOrders.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get order history by user " + user, e);
        }
    }
}
