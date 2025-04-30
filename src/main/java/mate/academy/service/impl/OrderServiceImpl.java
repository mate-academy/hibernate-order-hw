package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final ShoppingCartService shoppingCartService;

    public OrderServiceImpl(OrderDao orderDao, ShoppingCartService shoppingCartService) {
        this.orderDao = orderDao;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCartService.getByUser(order.getUser()));
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
