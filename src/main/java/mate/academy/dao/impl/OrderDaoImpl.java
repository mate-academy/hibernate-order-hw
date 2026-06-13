package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
            Root<Order> orderRoot = criteria.from(Order.class);
            orderRoot.fetch("user", JoinType.LEFT);
            orderRoot.fetch("tickets", JoinType.LEFT);
            Predicate userPredicate = builder.equal(orderRoot.get("user"), user);

            criteria.where(userPredicate);

            return session
                    .createQuery(criteria)
                    .getResultList();

        } catch (Exception e) {
            throw new DataProcessingException("Can't get order by user " + user, e);
        }
    }

    @Override
    public Order create(Order order) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DataProcessingException("Can't create order " + order, e);
        }
    }
}
