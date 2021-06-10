package mate.academy.service.impl;

import java.time.LocalDate;
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
    private ShoppingCartService cartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order(LocalDate.now(), shoppingCart.getUser());
        shoppingCart.getTickets().forEach(t -> order.getTickets().add(t));
        cartService.clearShoppingCart(shoppingCart);
        return orderDao.save(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
