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
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try {
            return HibernateUtil.getSessionFactory()
                    .fromSession(s -> s.createQuery(byUserQuery(), Order.class)
                            .setParameter("user", user)
                            .getResultList());
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders for user: " + user, e);
        }
    }

    private String byUserQuery() {
        return "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "JOIN FETCH o.user u "
                + "JOIN FETCH t.movieSession ms "
                + "JOIN FETCH ms.movie m "
                + "JOIN FETCH ms.cinemaHall c "
                + "WHERE o.user = :user";
    }
}
