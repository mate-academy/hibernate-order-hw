package mate.academy.dao.impl;

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
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory()
                    .openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add new order."
                    + order);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Order> get(Long id) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            return session.createQuery("From Order o where "
                + "id = :id", Order.class).setParameter("id", id)
                .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get Order with"
                    + id, e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            return session.createQuery("From Order o",
                    Order.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all Orders", e);
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            return session.createQuery("select distinct o From Order o "
                            + "left join fetch o.user "
                                    + "left join fetch o.tickets "
                                    + " where o.user = :user",
                    Order.class).setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all "
                    + "Orders of current user.", e);
        }
    }
}
