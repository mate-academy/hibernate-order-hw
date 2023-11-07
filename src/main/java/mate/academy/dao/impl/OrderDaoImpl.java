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
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("select o from Order o "
                            + "left join fetch o.tickets t "
                    + "left join fetch t.movieSession m "
                    + "left join fetch m.movie "
                    + "left join fetch m.cinemaHall "
                    + "where o.user = :user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new DataProcessingException(String
                    .format("Can't get order by user: " + user), e);
        }
    }

    @Override
    public Order add(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                session.save(order);
                transaction.commit();
                return order;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DataProcessingException(String.format("Can't add order: " + order), e);
            }
        }
    }
}
