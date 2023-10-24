package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl extends AbstractDao implements OrderDao {
    @Override
    public Order add(Order order) {
        return performReturningWithinTx(session -> {
            try {
                session.persist(order);
                return order;
            } catch (Exception e) {
                throw new DataProcessingException("Can't insert order", e);
            }
        });
    }

    @Override
    public List<Order> getByUser(User user) {
        try {
            return performReturningWithoutTx(session ->
                    getByUserQuery(session).setParameter("user", user).getResultList()
            );
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders by user: " + user, e);
        }
    }

    private Query<Order> getByUserQuery(Session session) {
        return session.createQuery("""
               from Order o
               join fetch o.user
               left join fetch o.tickets ot
               left join fetch ot.movieSession ms
               left join fetch ms.cinemaHall
               left join fetch ms.movie
               where o.user = :user""", Order.class);
    }
}
