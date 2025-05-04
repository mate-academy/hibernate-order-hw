package mate.academy.dao.impl;

import java.util.List;
import java.util.Objects;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new DataProcessingException(String.format("Can't "
                    + "add %s to DB", order), e);
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = getSession()) {
            return session.createQuery("select distinct o "
                            + "from Order o "
                            + "join fetch o.tickets t "
                            + "join fetch o.user "
                            + "join fetch t.movieSession ms "
                            + "join fetch ms.cinemaHall "
                            + "join fetch ms.movie "
                            + "where o.user = :user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(String.format("Can't "
                    + "get order by %s", user), e);
        }
    }

    private Session getSession() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        return sessionFactory.openSession();
    }
}
