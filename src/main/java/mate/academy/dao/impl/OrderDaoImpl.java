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
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save order " + order
                    + " to DB.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getAllByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> getOrdersHistoryQuery = session.createQuery(
                    "SELECT DISTINCT o FROM Order o "
                    + "LEFT JOIN FETCH o.ticketList tl "
                    + "LEFT JOIN FETCH o.user u "
                    + "LEFT JOIN FETCH tl .movieSession ms "
                    + "LEFT JOIN FETCH ms.movie "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "WHERE o.user.id = :userId", Order.class);
            getOrdersHistoryQuery.setParameter("userId", user.getId());
            return getOrdersHistoryQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders history by user "
                    + user + " from DB.", e);
        }
    }
}
