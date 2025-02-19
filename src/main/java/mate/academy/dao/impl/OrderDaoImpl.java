package mate.academy.dao.impl;

import mate.academy.dao.OrderDao;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    private final Session session = HibernateUtil.getSessionFactory().openSession();

    @Override
    public Order getByUser(User user) {
        Query<Order> query = session.createQuery(
                "FROM Order o JOIN FETCH o.tickets "
                        + " WHERE o.user = :user",
                Order.class);
        query.setParameter("user", user);
        return query.uniqueResult();
    }

    @Override
    public Order completeOrder(Order order) {
        Transaction transaction = session.beginTransaction();
        session.persist(order);
        transaction.commit();
        return order;
    }
}
