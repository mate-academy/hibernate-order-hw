package mate.academy.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
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

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            Order order = new Order();
            List<Ticket> tickets = new ArrayList<>(shoppingCart.getTickets());
            order.setTickets(tickets);
            order.setUser(shoppingCart.getUser());
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't complete order by shopping cart=" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            Fetch<Object, Object> getTickets = root.fetch("tickets", JoinType.LEFT);
            Fetch<Object, Object> getMovieSession = getTickets.fetch("movieSession", JoinType.LEFT);
            getMovieSession.fetch("cinemaHall", JoinType.LEFT);
            getMovieSession.fetch("movie", JoinType.LEFT);
            query.where(criteriaBuilder.equal(root.get("user"), user));
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get result by user=" + user, e);
        }
    }
}
