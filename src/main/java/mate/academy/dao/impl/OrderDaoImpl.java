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
    public List<Order> getAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> ordersFromDb = session.createQuery("SELECT distinct o FROM Order o "
                    + "INNER JOIN FETCH o.user u "
                    + "INNER JOIN FETCH o.tickets t "
                    + "INNER JOIN FETCH t.movieSession ms "
                    + "INNER JOIN FETCH ms.cinemaHall "
                    + "INNER JOIN FETCH ms.movie "
                    + "where o.user = :user", Order.class);
            ordersFromDb.setParameter("user", user);
            return ordersFromDb.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders by user = " + user, e);
        }
    }

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
            throw new DataProcessingException("Can't insert a order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
