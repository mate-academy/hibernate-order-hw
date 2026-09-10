package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = cb.createQuery(Order.class);
            Root<Order> orderRoot = query.from(Order.class);
            Fetch<Order, Ticket> ticketsFetch = orderRoot.fetch("tickets", JoinType.INNER);
            Fetch<Ticket, MovieSession> movieSessionsFetch = ticketsFetch
                    .fetch("movieSession", JoinType.INNER);
            movieSessionsFetch.fetch("movie", JoinType.INNER);
            movieSessionsFetch.fetch("cinemaHall", JoinType.INNER);
            orderRoot.fetch("user", JoinType.INNER);
            Predicate orderPredicate = cb.equal(orderRoot.get("user"), user);
            query.where(orderPredicate);
            query.distinct(true);
            query.orderBy(cb.desc(orderRoot.get("orderDate")));
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order by user: " + user, e);
        }
    }
}
