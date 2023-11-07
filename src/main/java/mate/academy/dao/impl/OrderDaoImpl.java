package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
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
    public Order add(Order order) {
        Transaction transaction = null;
        Session session = null;
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
            throw new DataProcessingException("Can't insert a order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getUserSessionByShoppingCartQuery =
                    session.createQuery("FROM Order or "
                            + "LEFT JOIN FETCH or.tickets tl "
                            + "LEFT JOIN FETCH or.user "
                            + "LEFT JOIN FETCH or.orderDate "
                            + "WHERE or.user = :user", Order.class);
            getUserSessionByShoppingCartQuery.setParameter("user", user);
            return getUserSessionByShoppingCartQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a Order session by user: "
                    + user, e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaQuery<Order> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Order.class);
            criteriaQuery.from(Order.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all Orders", e);
        }
    }
}
