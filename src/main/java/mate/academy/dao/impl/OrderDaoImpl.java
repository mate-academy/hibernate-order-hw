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
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not complete order,"
                    + " using the shopping cart: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH t.movieSession ms "
                    + "LEFT JOIN FETCH t.user "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "LEFT JOIN FETCH ms.movie m "
                    + "WHERE o.user.id = :id");
            query.setParameter("id", user.getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can not get list of orders by this user: "
                    + user, e);
        }
    }
}
