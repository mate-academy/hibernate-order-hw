package mate.academy.dao.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order: " + order + " to Database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> getQuery = cb.createQuery(Order.class);
            Root<Order> orderRoot = getQuery.from(Order.class);
            Fetch<Order, Ticket> ticketFetch =
                    orderRoot.fetch("tickets", JoinType.LEFT);
            orderRoot.fetch("user", JoinType.LEFT);
            Fetch<Ticket, MovieSession> movieSessionFetch =
                    ticketFetch.fetch("movieSession", JoinType.LEFT);
            movieSessionFetch.fetch("movie", JoinType.LEFT);
            movieSessionFetch.fetch("cinemaHall", JoinType.LEFT);
            getQuery.where(cb.equal(orderRoot.get("user"), user)).distinct(true);
            return session.createQuery(getQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get order history by user: " + user + "from Database ", e);
        }
    }
}
