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
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> ordersQuery = session.createQuery("from Order o "
                    + "LEFT JOIN FETCH o.tickets ots "
                    + "LEFT JOIN FETCH o.user "
                    + "LEFT JOIN FETCH ots.movieSession otsmvs "
                    + "LEFT JOIN FETCH otsmvs.movie "
                    + "LEFT JOIN FETCH otsmvs.cinemaHall "
                    + "WHERE o.user =:user", Order.class);
            ordersQuery.setParameter("user", user);
            return ordersQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find a orders by user: " + user, e);
        }
    }

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert a order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
