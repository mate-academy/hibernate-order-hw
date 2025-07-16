package mate.academy.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
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
    public Order completeOrder(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Order order = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            order = new Order();
            transaction = session.beginTransaction();
            order.setTickets(shoppingCart.getTickets());
            order.setUser(shoppingCart.getUser());
            order.setOrderDate(LocalDateTime.now());
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't complete the order: " + order, e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                    "SELECT DISTINCT o FROM Order o "
                            + "JOIN FETCH o.user "
                            + "JOIN FETCH o.tickets t "
                            + "JOIN FETCH t.movieSession ms "
                            + "JOIN FETCH ms.movie "
                            + "JOIN FETCH ms.cinemaHall "
                            + "WHERE o.user = :user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't retrieve orders for user: " + user, e);
        }
    }
}
