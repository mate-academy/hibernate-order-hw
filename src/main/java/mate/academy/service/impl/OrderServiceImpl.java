package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
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
        order.setTickets(new ArrayList<>(List.copyOf(shoppingCart.getTickets())));
        order.setUser(shoppingCart.getUser());
        order.setLocalDateTime(LocalDateTime.now());
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> orders = new ArrayList<>();
        Optional<Order> orderFromDb = orderDao.getByUser(user);
        if (orderFromDb.isPresent()) {
            Order order = orderFromDb.get();
            orders.add(order);
            return orders;
        }
        throw new DataProcessingException("Can not find order by user "
                + user);
    }
}
