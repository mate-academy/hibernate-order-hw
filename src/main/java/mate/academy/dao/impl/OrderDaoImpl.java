package mate.academy.dao.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save order " + order + " to DB.", e);

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
            Root<Order> root = query.from(Order.class);
            Fetch<Order, User> orderUserFetch = root.fetch("user");
            Fetch<Order, Ticket> orderTicketFetch = root.fetch("tickets", JoinType.LEFT);
            Fetch<Ticket, MovieSession> ticketMovieSessionFetch
                    = orderTicketFetch.fetch("movieSession", JoinType.LEFT);
            Fetch<MovieSession, Movie> movieSessionMovieFetch
                    = ticketMovieSessionFetch.fetch("movie", JoinType.LEFT);
            Fetch<MovieSession, CinemaHall> movieSessionCinemaHallFetch
                    = ticketMovieSessionFetch.fetch("cinemaHall", JoinType.LEFT);
            query.where(cb.equal(root.get("user"), user));
            return session.createQuery(query).getResultList();
        }
    }
}
