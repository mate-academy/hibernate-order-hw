package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private ShoppingCartService shoppingCartService;
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        List<Ticket> shoppingCartTickets = shoppingCart.getTickets();
        Order order = new Order();
        order.setTickets(new ArrayList<Ticket>(shoppingCartTickets));
        shoppingCartService.clearShoppingCart(shoppingCart);
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        orderDao.add(order);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
