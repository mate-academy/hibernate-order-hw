package mate.academy.dao.impl;

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
import java.util.List;

@Dao
public class OrderDaoImpl implements OrderDao {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Order save(Order order) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataProcessingException(String.format(
                    "Can't add order: %s to DB", order), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = factory.openSession()) {
            Query<Order> getOrderByUserQuery
                    = session.createQuery("FROM Order o " +
                    "LEFT JOIN FETCH o.tickets t " +
                    "LEFT JOIN FETCH o.user u " +
                    "LEFT JOIN FETCH t.movieSession ms " +
                    "LEFT JOIN FETCH ms.movie " +
                    "LEFT JOIN FETCH ms.cinemaHall " +
                    "WHERE o.user = :user", Order.class);
            getOrderByUserQuery.setParameter("user", user);
            return getOrderByUserQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders for user: " + user, e);
        }
    }
}
