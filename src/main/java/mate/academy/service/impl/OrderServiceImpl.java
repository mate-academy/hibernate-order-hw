package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        LocalDateTime timeNow = LocalDateTime.now();
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(timeNow);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getAll();
    }
}
