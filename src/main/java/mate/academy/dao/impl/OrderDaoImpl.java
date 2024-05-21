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
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cannot add order: " + order, e);
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
            Query<Order> createOrderQuery = session.createQuery("from Order o "
                    + "JOIN FETCH o.tickets t "
                    + "JOIN FETCH t.movieSession ms "
                    + "JOIN FETCH ms.cinemaHall "
                    + "JOIN FETCH ms.movie "
                    + "WHERE o.user = :user", Order.class);
            createOrderQuery.setParameter("user", user);
            return createOrderQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get orders by user: " + user, e);
        }
    }
}
