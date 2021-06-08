package mate.academy.dao.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.MovieSessionDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class MovieSessionDaoImpl implements MovieSessionDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public MovieSession add(MovieSession movieSession) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(movieSession);
            transaction.commit();
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save current movie session: "
                    + movieSession, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<MovieSession> get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<MovieSession> getByIdQuery = session
                    .createQuery("FROM MovieSession ms "
                            + "LEFT JOIN FETCH ms.movie "
                            + "LEFT JOIN FETCH ms.cinemaHall "
                            + "WHERE ms.id = :id", MovieSession.class);
            getByIdQuery.setParameter("id", id);
            return Optional.ofNullable(getByIdQuery.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't take movie session by current ID: " + id, e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            Query<MovieSession> findAllMovieSessionQuery = session
                    .createQuery("FROM MovieSession ms "
                                    + "LEFT JOIN FETCH ms.movie "
                                    + "LEFT JOIN FETCH ms.cinemaHall "
                                    + "WHERE ms.movie.id = :id "
                                    + "AND ms.showTime BETWEEN :startOfDay AND :endOfDay",
                            MovieSession.class);
            findAllMovieSessionQuery.setParameter("id", movieId);
            findAllMovieSessionQuery.setParameter("startOfDay", date.atTime(LocalTime.MIDNIGHT));
            findAllMovieSessionQuery.setParameter("endOfDay", date.atTime(LocalTime.MAX));
            return findAllMovieSessionQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't take all of available sessions by "
                    + "current movie ID: " + movieId + ", and current date: " + date, e);
        }
    }
}
