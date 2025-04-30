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
    public Order add(Order newOrder) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(newOrder);
            transaction.commit();
            return newOrder;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert order: " + newOrder, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Order o "
                    + "INNER JOIN FETCH o.user u "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "INNER JOIN FETCH t.movieSession ms "
                    + "INNER JOIN FETCH ms.movie "
                    + "INNER JOIN FETCH ms.cinemaHall "
                    + "WHERE o.user =:user";
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order history of user: " + user, e);
        }
    }
}
