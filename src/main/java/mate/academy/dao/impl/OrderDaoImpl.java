package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaQuery;
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
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("can't save order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Order getByUser(User user) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("from Order o " +
                    "left join fetch o.tickets t " +
                    "left join fetch t.movieSession ms " +
                    "left join fetch ms.movie " +
                    "left join fetch ms.cinemaHall " +
                    "left join fetch o.user " +
                    "where o.user = :user");
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DataProcessingException("can't resolve order by user: " + user, e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaQuery<Order> query = session.getCriteriaBuilder().createQuery(Order.class);
            query.from(Order.class);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("can't get all from order", e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("from Order o " +
                    "left join fetch o.tickets t " +
                    "left join fetch t.movieSession ms " +
                    "left join fetch ms.movie " +
                    "left join fetch ms.cinemaHall " +
                    "left join fetch o.user " +
                    "where o.user.id = :userId");
            query.setParameter("userId", user.getId());
            return query.getResultList();
        }
    }

    @Override
    public void update(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("can't save order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
