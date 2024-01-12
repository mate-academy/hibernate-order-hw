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
    private static final String HQL = "FROM Order o "
            + "LEFT JOIN FETCH o.user "
            + "LEFT JOIN FETCH o.tickets t "
            + "LEFT JOIN FETCH t.movieSession ms "
            + "LEFT JOIN FETCH ms.movie "
            + "LEFT JOIN FETCH ms.cinemaHall "
            + "WHERE o.user =:user";

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
            throw new DataProcessingException("Can't save order: "
                    + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> findAll(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(HQL, Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get list of orders by user: "
                    + user, e);
        }
    }

    @Override
    public void update(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update order: "
                    + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
