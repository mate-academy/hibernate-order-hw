package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;

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
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Database constraint violation while "
                    + "inserting order. Check if all related entities exist: " + order, e);
        } catch (JDBCConnectionException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Database connection error while inserting "
                    + "order: " + order, e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getByUser(User user) {
        if (user == null) {
            throw new DataProcessingException("User cannot be null",
                    new IllegalArgumentException());
        }
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH t.movieSession ms "
                    + "LEFT JOIN FETCH ms.movie "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "WHERE o.user = :user "
                    + "ORDER BY o.orderDate DESC", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (JDBCConnectionException e) {
            throw new DataProcessingException("Database connection error while "
                    + "finding orders for user: " + user, e);
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders for user: " + user, e);
        }
    }

    @Override
    public Order get(Long id) {
        if (id == null) {
            throw new DataProcessingException("Order ID cannot be null",
                    new IllegalArgumentException());
        }
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o "
                    + "LEFT JOIN FETCH o.tickets t "
                    + "LEFT JOIN FETCH t.movieSession ms "
                    + "LEFT JOIN FETCH ms.movie "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "WHERE o.id = :id", Order.class);
            query.setParameter("id", id);

            List<Order> results = query.getResultList();
            if (results.isEmpty()) {
                throw new DataProcessingException("Order not found with id: " + id,
                        new IllegalStateException());
            }
            return results.get(0);
        } catch (JDBCConnectionException e) {
            throw new DataProcessingException("Database connection error while "
                    + "finding order with id: " + id, e);
        } catch (Exception e) {
            throw new DataProcessingException("Can't find order with id: " + id, e);
        }
    }
}
