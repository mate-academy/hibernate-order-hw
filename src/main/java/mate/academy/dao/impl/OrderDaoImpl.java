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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        Order order = new Order(
                shoppingCart.getTickets(),
                LocalDateTime.now(),
                shoppingCart.getUser()
        );
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can`t complete order of shopping cart " + shoppingCart, e);
        }
        if (session != null) {
            session.close();
        }
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order o "
                            + "LEFT JOIN FETCH o.user u "
                            + "LEFT JOIN FETCH o.tickets t "
                            + "LEFT JOIN FETCH t.movieSession ms "
                            + "LEFT JOIN FETCH ms.cinemaHall "
                            + "LEFT JOIN FETCH ms.movie "
                            + "WHERE o.user = :user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (HibernateException e) {
            throw new DataProcessingException("Can`t get orders of user " + user, e);
        }
    }
}
