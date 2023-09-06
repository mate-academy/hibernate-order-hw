package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateException;
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
            return order;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save to DB order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getAllOrdersQuery =
                    session.createQuery("SELECT DISTINCT o FROM Order o "
                                        + "LEFT JOIN FETCH o.user "
                                        + "LEFT JOIN FETCH o.tickets t "
                                        + "LEFT JOIN FETCH  t.movieSession ms "
                                        + "LEFT JOIN FETCH  ms.movie "
                                        + "LEFT JOIN FETCH  ms.cinemaHall "
                                        + "WHERE o.user.id = :userId", Order.class);
            getAllOrdersQuery.setParameter("userId", user.getId());
            return getAllOrdersQuery.getResultList();
        } catch (HibernateException e) {
            throw new DataProcessingException("Can't get orders from DB for user: " + user, e);
        }
    }
}
