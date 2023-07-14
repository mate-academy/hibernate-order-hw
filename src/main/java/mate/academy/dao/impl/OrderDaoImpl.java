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
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
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
            throw new DataProcessingException("Could not add order to DB:" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery(
                            "SELECT distinct o FROM Order o "
                                    + "JOIN o.user u WITH u = :user "
                                    + "LEFT JOIN FETCH o.tickets t "
                                    + "LEFT JOIN FETCH t.movieSession ms "
                                    + "LEFT JOIN FETCH ms.movie m "
                                    + "LEFT JOIN FETCH ms.cinemaHall ch "
                                    + "JOIN FETCH o.user",
                            Order.class)
                    .setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find any order by user: " + user, e);
        }
    }
}
