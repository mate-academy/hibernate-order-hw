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
    public Order create(Order order) {
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
            throw new DataProcessingException("Can't create new order:" + order, e);
        } finally {
            if (session != null) {
                session.close();

            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getOrderHistoryQuery = session.createQuery("SELECT DISTINCT o "
                    + "FROM Order o "
                    + "JOIN FETCH o.user "
                    + "JOIN FETCH o.tickets t "
                    + "JOIN FETCH t.movieSession ms "
                    + "JOIN FETCH ms.cinemaHall "
                    + "JOIN FETCH ms.movie "
                    + "WHERE o.user = :user ", Order.class);
            getOrderHistoryQuery.setParameter("user", user);
            return getOrderHistoryQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders history by user: " + user, e);
        }
    }
}
