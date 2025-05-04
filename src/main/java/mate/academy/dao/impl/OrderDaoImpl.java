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
    public List<Order> getAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select distinct o from Order o "
                            + "inner join fetch o.tickets t "
                            + "inner join fetch t.user u "
                            + "inner join fetch t.movieSession ms "
                            + "inner join fetch ms.movie m "
                            + "inner join fetch ms.cinemaHall ch "
                            + "where o.user.id = :userId", Order.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get orders by user: "
                    + user + " from DB. ", e);
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
            throw new DataProcessingException("Couldn't add order: " + order + " to DB. ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
