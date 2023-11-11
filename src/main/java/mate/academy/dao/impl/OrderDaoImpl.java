package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
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
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Order.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't get an order by id: " + id, e);
        }
    }

    @Override
    public List<Order> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaQuery<Order> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Order.class);
            criteriaQuery.from(Order.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all orders", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = criteriaQuery.from(Order.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("user"), user));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders by user: " + user, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
