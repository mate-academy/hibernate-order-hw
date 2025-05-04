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
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert order: " + order, e);
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getAllOrdersQuery =
                    session.createQuery("SELECT DISTINCT o FROM Order o "
                            + "LEFT JOIN FETCH o.user u "
                            + "LEFT JOIN FETCH o.tickets t "
                            + "LEFT JOIN FETCH  t.movieSession ms "
                            + "LEFT JOIN FETCH  ms.movie m "
                            + "LEFT JOIN FETCH  ms.cinemaHall ch "
                            + "WHERE o.user.id = :userId", Order.class);
            getAllOrdersQuery.setParameter("userId", user.getId());
            return getAllOrdersQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders from DB by user: " + user, e);
        }
    }
}
