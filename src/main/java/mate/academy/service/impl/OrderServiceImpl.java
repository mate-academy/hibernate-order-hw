package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
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
        Order orderToComplete = new Order();
        orderToComplete.setOrderDate(LocalDateTime.now());
        orderToComplete.setUser(shoppingCart.getUser());
        orderToComplete.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        Order complittedOrder = orderDao.add(orderToComplete);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return complittedOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
