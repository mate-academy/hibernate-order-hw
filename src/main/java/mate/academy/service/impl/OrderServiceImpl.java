package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private OrderDao orderDao;
    @Inject
    private UserDao userDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        if (userDao.findByEmail(shoppingCart.getUser().getEmail()).isEmpty()) {
            try {
                authenticationService.register(shoppingCart.getUser().getEmail(),
                        shoppingCart.getUser().getPassword());
            } catch (RegistrationException e) {
                throw new RuntimeException(e);
            }
        }
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setTickets(shoppingCart.getTickets());
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return List.of(orderDao.getByUser(user));
    }
}
