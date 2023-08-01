package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImp implements OrderDao {
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
            throw new DataProcessingException("can`t add order to db" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> findOrderByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> findByUser = session.createQuery("FROM Order o "
                    + " JOIN FETCH o.tickets t "
                    + " JOIN FETCH t.movieSession mS "
                    + " LEFT JOIN FETCh mS.movie "
                    + " LEFT JOIN FETCH mS.cinemaHall "
                    + " WHERE o.user = :user ", Order.class);
            findByUser.setParameter("user", user);
            return findByUser.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("can`t get order by user" + user, e);
        }
    }
}
