package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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
            throw new DataProcessingException("Failed to create order " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Order getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Order.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a Order by id: " + id, e);
        }
    }

    @Override
    public List<Order> getByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Order> orderList = session.createQuery(
                            "SELECT o FROM Order o "
                                    + "JOIN FETCH o.user "
                                    + "WHERE o.user.id = :userId", Order.class)
                    .setParameter("userId", userId)
                    .getResultList();
            return orderList; // повертаємо користувача
        } catch (Exception e) {
            throw new DataProcessingException("Failed to get user with id: " + userId, e);
        }
    }
}
