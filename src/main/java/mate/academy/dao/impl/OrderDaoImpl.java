package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class OrderDaoImpl implements OrderDao {
    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("select * from Order as o "
                    + "left join fetch o.tickets t "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch t.user "
                    + "left join fetch ms.movie "
                    + "left join fetch ms.cinemaHall "
                    + "where o.user.id = :user_id", Order.class);
            query.setParameter("user_id", user.getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't fined orders in DB for user=" + user, e);
        }
    }
}
