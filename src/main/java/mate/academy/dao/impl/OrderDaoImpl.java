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
    private static final SessionFactory factory =
            HibernateUtil.getSessionFactory();

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("An error occurred while "
                    + "processing query to add new order = " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        try (Session session = factory.openSession()) {
            Query<Order> queryToGetOrderByUser = session.createQuery("from Order o "
                    + "left join fetch o.user "
                    + "left join fetch o.tickets t "
                    + "left join fetch t.cinemaHall "
                    + "left join fetch t.movie "
                    + "left join fetch t.user "
                    + "where o.user = :user", Order.class);
            queryToGetOrderByUser.setParameter("user", user);
            return queryToGetOrderByUser.getResultList();
        }
    }
}
