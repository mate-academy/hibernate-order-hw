package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setUser(shoppingCart.getUser());
        order.setLocalDateTime(LocalDateTime.now());
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> orders = orderDao.getUserOrders(user);
        orders.sort(Comparator.comparing(Order::getLocalDateTime, Comparator.reverseOrder()));
        return orders;
    }
}
