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
        if (shoppingCart == null || shoppingCart.getTickets().isEmpty()) {
            throw new RuntimeException("Invalid shopping cart!" + shoppingCart);
        }
        Order newOrder = new Order();
        newOrder.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setUser(shoppingCart.getUser());

        shoppingCartService.clearShoppingCart(shoppingCart);

        return orderDao.add(newOrder);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> userOrders = orderDao.getUserOrders(user);
        return userOrders;
    }
}
