package mate.academy.service.impl;

import mate.academy.dao.OrderDao;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = creatOrderFromShoppingCart(shoppingCart);
        orderDao.add(order);
        shoppingCart.getTickets().clear();
        return order;
    }

    private Order creatOrderFromShoppingCart(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setOrderDate(LocalDateTime.now());
        order.setUser(shoppingCart.getUser());
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersByUser(user);
    }
}
