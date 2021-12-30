package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Couldn't save order to db. Order: " + order,
                    exception);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("from Order o "
                    + "join fetch o.tickets t "
                    + "join fetch t.movieSession ms "
                    + "join fetch t.user "
                    + "join fetch ms.movie "
                    + "join fetch ms.cinemaHall "
                    + "where o.user = :user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception exception) {
            throw new DataProcessingException("Couldn't get all order by user: " + user,
                    exception);
        }
    }
}
