package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User managedUser = session.merge(order.getUser());
            order.setUser(managedUser);
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Order> query = session.createQuery("FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE o.user =:user", Order.class);
            query.setParameter("user", user);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find an order by user: " + user, e);
        }
    }
}
