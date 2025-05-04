package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
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
    private OrderDao orderDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        ArrayList<Ticket> orderTickets = new ArrayList<>(shoppingCart.getTickets());
        order.setTickets(orderTickets);
        Order savedOrder = orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return savedOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersByUser(user);
    }
}
