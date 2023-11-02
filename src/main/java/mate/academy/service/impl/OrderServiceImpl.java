package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private ShoppingCartService shoppingCartService;
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = createOrder(shoppingCart);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDao.add(order);
            shoppingCartService.clearShoppingCart(shoppingCart);
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Сan't complete order by shoppingCart" + order);
        }
    }

    private Order createOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(shoppingCart.getUser());
        order.setTickets(shoppingCart.getTickets());
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> sessionQuery
                    = session.createQuery("select o from Order o "
                    + "left join fetch o.tickets "
                    + "where o.user.id = :id", Order.class);
            sessionQuery.setParameter("id", user.getId());
            return sessionQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get orders by user " + user);
        }
    }
}
