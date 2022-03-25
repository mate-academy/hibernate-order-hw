package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateException;
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
            session.save(order);
            transaction.commit();
            return order;
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create order in DB: " + order, ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Order> getListOrderByQuery = session.createQuery("from Order o "
                    + "left join o.user "
                    + "left join o.tickets t "
                    + "left join t.movieSession ms "
                    + "left join ms.movie "
                    + "left join ms.cinemaHall "
                    + "where o.user = :user", Order.class);
            getListOrderByQuery.setParameter("user", user);
            return getListOrderByQuery.getResultList();
        } catch (HibernateException ex) {
            throw new DataProcessingException("Can't get list of orders: " + user, ex);
        }
    }
}
