package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.UserNotFoundException;
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
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        shoppingCart.getTickets().clear();
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) throws UserNotFoundException {
        List<Order> ordersByUser = orderDao.getByUser(user);
        if (!ordersByUser.isEmpty()) {
            return ordersByUser;
        }
        throw new UserNotFoundException("Orders for user: " + user + ", is not found");
    }
}
