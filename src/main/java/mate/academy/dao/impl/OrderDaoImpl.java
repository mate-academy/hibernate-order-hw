package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

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
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    String.format("Can`t add a new order: %s to the DB", order), ex);
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
            return session.createQuery("FROM Order o "
                            + "JOIN FETCH o.tickets t "
                            + "JOIN FETCH t.movieSession ms "
                            + "JOIN FETCH ms.movie "
                            + "JOIN FETCH ms.cinemaHall "
                            + "JOIN FETCH o.user u "
                            + "WHERE u.id = :userId", Order.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
        } catch (Exception ex) {
            throw new DataProcessingException(
                    String.format("Can`t get orders by user: %s", user), ex
            );
        }
    }
}
