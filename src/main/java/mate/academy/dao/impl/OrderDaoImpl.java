package mate.academy.dao.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service
public class OrderDaoImpl implements OrderDao {
    @Override
    public List<Order> getOrdersByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = criteriaQuery.from(Order.class);
            Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);
            criteriaQuery.select(root).where(userPredicate);
            return session.createQuery(criteriaQuery).getResultList();

        } catch (Exception e) {
            throw new DataProcessingException("Can't get a Orders by user: " + user, e);
        }
    }

    @Override
    public Order addOrder(Order order) {
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
            throw new DataProcessingException("Can't save order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
