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

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Transaction transaction = null;
        Session session = null;
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
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (session.createQuery("FROM Order o "
                            + "LEFT JOIN FETCH o.user "
                            + "LEFT JOIN FETCH o.tickets t "
                            + "LEFT JOIN FETCH t.movieSession ms "
                            + "LEFT JOIN FETCH ms.cinemaHall "
                            + "LEFT JOIN FETCH ms.movie "
                            + "WHERE o.user.id = :userId", Order.class)
                            .setParameter("userId", user.getId()).getResultList());
        } catch (Exception e) {
            throw new DataProcessingException("Can't get an order by id: " + user.getId(), e);
        }
    }
}
