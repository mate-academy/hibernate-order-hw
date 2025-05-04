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
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order AS o "
                    + "LEFT OUTER JOIN FETCH o.tickets AS t "
                    + "LEFT OUTER JOIN FETCH t.movieSession AS ms "
                    + "LEFT OUTER JOIN FETCH ms.movie "
                    + "LEFT OUTER JOIN FETCH ms.cinemaHall "
                    + "where o.user.email = :email", Order.class);
            query.setParameter("email", user.getEmail());
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot fetch a list of orders for user + "
                    + user, e);
        }
    }
}
