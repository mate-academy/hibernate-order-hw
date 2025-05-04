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
    private SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Order addOrder(Order order) {
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
            throw new DataProcessingException("Can't add new order to DB with param "
                    + order + ". ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrderByUser(User user) {
        String query = "FROM Order o "
                + "left join fetch o.tickets t "
                + "left join fetch t.movieSession tms "
                + "left join fetch tms.cinemaHall "
                + "left join fetch tms.movie WHERE o.user = :user";
        try (Session session = factory.openSession()) {
            Query<Order> queryByUser = session.createQuery(query);
            queryByUser.setParameter("user", user);
            return queryByUser.list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order by user with param "
                    + user + ". ", e);
        }
    }
}
