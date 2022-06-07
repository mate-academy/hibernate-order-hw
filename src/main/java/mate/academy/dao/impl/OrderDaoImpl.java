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
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order in DB: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistoryByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderHistoryByUser = session.createQuery("from Order o "
                    + "inner join fetch o.tickets t "
                    + "inner join fetch t.movieSession ms "
                    + "inner join fetch ms.movie m "
                    + "inner join fetch ms.cinemaHall mh"
                    + "inner join fetch o.user u "
                    + "where u.id = :userId", Order.class);
            getOrderHistoryByUser.setParameter("userId",user.getId());
            return getOrderHistoryByUser.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order history for user: " + user, e);
        }
    }
}
