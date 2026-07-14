package mate.academy.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                    "from Order as o left join fetch o.tickets "
                            + "where o.user.id = :user_id",Order.class);
            query.setParameter("user_id", user.getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can not get the orders from user", e);
        }
    }

    @Override
    public Order addOrder(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.getTransaction();
            transaction.begin();
            Order order = new Order();
            order.setUser(shoppingCart.getUser());
            order.setOrderDate(LocalDateTime.now());
            if (shoppingCart.getTickets() != null) {
                List<Ticket> ticketsFromShoppingCart = new ArrayList();
                ticketsFromShoppingCart.addAll(shoppingCart.getTickets());
                order.setTickets(ticketsFromShoppingCart);
            }
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not insert Order to a DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
