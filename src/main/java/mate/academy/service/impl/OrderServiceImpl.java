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
        if (shoppingCart != null
                && shoppingCart.getTickets() != null
                && !shoppingCart.getTickets().isEmpty()) {
            Order order = new Order();
            order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
            order.setOrderDate(LocalDateTime.now());
            order.setUser(shoppingCart.getUser());
            shoppingCartService.clearShoppingCart(shoppingCart);
            return orderDao.add(order);
        }
        throw new RuntimeException("Shopping cart must not be empty " + shoppingCart);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
