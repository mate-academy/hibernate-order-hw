package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

public class OrderDaoImpl implements OrderDao {

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct o from Order o "
                    + "left join fetch o.tickets "
                    + "where o.user = :user ", Order.class)
                .setParameter("user", user)
                .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Failed to getByUser the Order "
                + "object with user: " + user, e);
        }
    }
}
