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
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from Order o "
                    + "left join fetch o.tickets t "
                    + "left join fetch o.user "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch ms.cinemaHall "
                    + "left join fetch  ms.movie "
                    + "where o.user =:user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
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
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add order into db" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
