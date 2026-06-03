package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaQuery;
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
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            transactionNullCheck(transaction);
            throw new DataProcessingException("Can't persist order: " + order, e);
        } finally {
            closeSession(session);
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = openSession()) {
            return session.createQuery("select distinct o "
                            + "from Order o"
                            + " left join fetch o.tickets"
                            + " where o.user = :user", Order.class)
                    .setParameter("user", user).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get order by user: " + user, e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Session session = openSession()) {
            CriteriaQuery<Order> cq = session.getCriteriaBuilder()
                    .createQuery(Order.class);
            cq.from(Order.class);
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get list of orders", e);
        }
    }

    private Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    private void transactionNullCheck(Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    private void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }
}
