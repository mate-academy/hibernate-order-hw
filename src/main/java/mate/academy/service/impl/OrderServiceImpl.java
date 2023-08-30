package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        if (shoppingCart == null || shoppingCart.getTickets().isEmpty()) {
            throw new RuntimeException("Check if you are authorized, "
                    + "choose your tickets and put them to cart");
        }
        Order order = new Order();
        List<Ticket> tickets = new ArrayList<>(shoppingCart.getTickets());
        order.setTickets(tickets);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(shoppingCart.getUser());
        Order newOrder = orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return newOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
