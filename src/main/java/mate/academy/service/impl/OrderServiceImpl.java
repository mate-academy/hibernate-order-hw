package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order comleteOrder(ShoppingCart shoppingCart) {
        try {
            Order order = new Order();
            List<Ticket> tickets = new ArrayList<>(shoppingCart.getTickets());
            order.setUser(shoppingCart.getUser());
            order.setTickets(tickets);
            order.setOrderDate(LocalDateTime.now());
            orderDao.add(order);
            shoppingCartService.clearShoppingCart(shoppingCart);
            return order;
        } catch (Exception e) {
            throw new DataProcessingException("Can't complete order by ShoppingCart: "
                                                + shoppingCart, e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return orderDao.getByUser(user);
        } catch (Exception e) {
            throw new DataProcessingException("Can't find orders by User: " + user, e);
        }
    }
}
