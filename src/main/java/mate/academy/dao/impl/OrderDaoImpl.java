package mate.academy.dao.impl;

import static mate.academy.util.HibernateUtil.getSessionFactory;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.lib.Inject;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            ShoppingCart shoppingCart = shoppingCartService.getByUser(order.getUser());
            shoppingCartService.clearShoppingCart(shoppingCart);
            return order;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order " + order + " to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Order> getOrdersByUser = session.createQuery("from Order o "
                    + "left join fetch o.user "
                    + "left join fetch o.tickets t "
                    + "left join fetch t.movieSession mv "
                    + "left join fetch mv.movie "
                    + "left join fetch mv.cinemaHall "
                    + "where o.user = :user", Order.class);
            getOrdersByUser.setParameter("user", user);
            return getOrdersByUser.getResultList();
        } catch (HibernateException e) {
            throw new DataProcessingException("Can't get orders by user " + user + " from DB", e);
        }
    }
}
