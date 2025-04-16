package mate.academy.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(ShoppingCart shoppingCart) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Order order = new Order();
        try {
            transaction = session.beginTransaction();
            order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
            order.setUser(shoppingCart.getUser());
            order.setOrderDate(LocalDateTime.now());
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Cannot complete order by shoppingCart" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            List<Order> orders = new ArrayList<>();
            return session.createQuery("from Order o LEFT JOIN FETCH o.tickets t"
                            + " LEFT JOIN FETCH t.movieSession m "
                            + "LEFT JOIN FETCH m.movie mm "
                            + "LEFT JOIN FETCH m.cinemaHall "
                            + "LEFT JOIN FETCH o.user u ",Order.class)
                    .getResultList();
        }
    }
}
