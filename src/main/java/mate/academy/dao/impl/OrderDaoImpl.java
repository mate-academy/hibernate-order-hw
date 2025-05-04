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
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
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

        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                    "from Order o "
                    + "left join fetch o.user u "
                    + "left join fetch o.tickets t "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch t.user tu "
                    + "left join fetch ms.movie m "
                    + "left join fetch ms.cinemaHall ch "
                    + "where u.id = :id", Order.class);
            query.setParameter("id", user.getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders for user: " + user, e);
        }
    }
}
