package mate.academy.dao.impl;

import java.util.List;
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

    @Override
    public Order add(Order order) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't add a new order to DB: " + order, e
            );
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        String getOrderByUserHql = "FROM Order o "
                + "LEFT JOIN FETCH o.user u "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie m "
                + "LEFT JOIN FETCH ms.cinemaHall ch "
                + "WHERE o.user = :user ";
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<Order> gerOrderByUserQuery =
                    session.createQuery(getOrderByUserHql, Order.class);
            gerOrderByUserQuery.setParameter("user", user);
            return gerOrderByUserQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get order by user: " + user, e
            );
        }
    }
}
