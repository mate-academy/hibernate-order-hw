package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cannot add an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        try (Session session = factory.openSession()) {
            Query<Order> query = session.createQuery("from Order o "
                    + "left join fetch o.tickets t "
                    + "left join fetch o.user "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch t.user "
                    + "left join fetch ms.movie "
                    + "left join fetch ms.cinemaHall "
                    + "where o.user = :user", Order.class);

            query.setParameter("id", id);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get order by id: " + id, e);
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = factory.openSession()) {
            Query<Order> query = session.createQuery("from Order o "
                    + "left join fetch o.tickets t "
                    + "left join fetch o.user "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch t.user "
                    + "left join fetch ms.movie "
                    + "left join fetch ms.cinemaHall "
                    + "where o.user = :user", Order.class);

            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get order by user: " + user, e);
        }
    }
}
