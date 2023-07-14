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
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Order o"
                            + " left join fetch o.tickets t"
                            + " left join fetch t.movieSession ms"
                            + " left join fetch ms.cinemaHall"
                            + " left join fetch ms.movie"
                            + " left join fetch o.user"
                            + " where o.user = :user", Order.class)
                    .setParameter("user", user).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders by user: " + user, e);
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
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert a Order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }
}
