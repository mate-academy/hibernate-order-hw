package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
        List<Ticket> tickets = shoppingCart.getTickets();
        shoppingCartService.clearShoppingCart(shoppingCart);
        Order order = new Order(LocalDateTime.now(), shoppingCart.getUser(), tickets);
        orderDao.add(order);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> userOrders = orderDao.getByUser(user);
        if (userOrders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for user " + user);
        }
        return userOrders;
    }
}
