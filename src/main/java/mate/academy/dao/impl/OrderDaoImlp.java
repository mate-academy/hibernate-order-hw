package mate.academy.dao.impl;

import java.util.Optional;
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
public class OrderDaoImlp implements OrderDao {

    @Override
    public Optional<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("select o from Order o where o.user = :user");
            query.setParameter("user", user);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(String
                    .format("Can't get order by user: " + user), e);
        }
    }

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
            throw new DataProcessingException(String.format("Can't add order: " + order), e);
        }
    }
}
