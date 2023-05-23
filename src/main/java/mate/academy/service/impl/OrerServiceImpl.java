package mate.academy.service.impl;

import java.time.LocalDateTime;
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
public class OrerServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order completedOrder = new Order(
                List.copyOf(shoppingCart.getTickets()),
                LocalDateTime.now(),
                shoppingCart.getUser());
        completedOrder = orderDao.add(completedOrder);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return completedOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
