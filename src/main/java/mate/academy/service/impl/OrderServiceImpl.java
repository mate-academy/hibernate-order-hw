package mate.academy.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

@Service
public class OrderServiceImpl implements OrderService {

    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(shoppingCart.getUser());
        order.setTickets(shoppingCart.getTickets());
        orderDao.add(order);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = criteriaQuery.from(Order.class);

            root.fetch("tickets", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession", JoinType.LEFT);
            root.fetch("tickets").fetch("user", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession").fetch("movie", JoinType.LEFT);
            root.fetch("tickets").fetch("movieSession").fetch("cinemaHall", JoinType.LEFT);

            Predicate orderPredicate = criteriaBuilder.equal(root.get("user").get("id"),
                    user.getId());

            criteriaQuery.select(root).where(orderPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get available orders for user: " + user.getId() + " for date: ", e);
        }
    }
}


