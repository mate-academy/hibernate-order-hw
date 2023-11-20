package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    private static final String GET_ORDERS_BY_USER =
            "FROM Order ord WHERE ord.user.id = user.id";
    private static final String CANT_ADD_ORDER_EXCEPTION_MESSAGE =
            "Can't add Order to DB: ";
    private static final String CANT_GET_ORDERS_EXCEPTION_MESSAGE =
            "Can't get list of Orders from DB for User: ";
    private static final String USER_PARAMETER = "user";

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(CANT_ADD_ORDER_EXCEPTION_MESSAGE + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = factory.openSession()) {
            Query<Order> getOrdersByUser = session.createQuery("FROM Order ord "
                    + "LEFT JOIN FETCH ord.user u "
                    + "LEFT JOIN FETCH ord.tickets t "
                    + "LEFT JOIN FETCH t.movieSession ms "
                    + "LEFT JOIN FETCH ms.movie "
                    + "LEFT JOIN FETCH ms.cinemaHall "
                    + "WHERE ord.user =:user", Order.class);
            getOrdersByUser.setParameter(USER_PARAMETER, user);
            return getOrdersByUser.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    CANT_GET_ORDERS_EXCEPTION_MESSAGE + user, e);
        }
    }
}
