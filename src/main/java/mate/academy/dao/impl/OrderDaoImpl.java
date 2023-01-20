package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class OrderDaoImpl implements OrderDao {
    @Override
    public List<Order> getAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String queryHql ="SELECT DISTINCT o FROM Order o"
                    + " INNER JOIN fetch o.tickets t INNER JOIN fetch t.user u"
                    + " INNER JOIN fetch t.movieSession ms INNER JOIN fetch ms.movie m"
                    + " INNER JOIN fetch ms.cinemaHall c"
                    + " WHERE o.user= :user";
            Query<Order> query = session.createQuery(queryHql, Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all orders by user: " + user, e);
        }
    }

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add order : " + order + " to DB. ", e);

        } finally {
           if (session != null) {
               session.close();
           }
        }
        return order;
    }
}
