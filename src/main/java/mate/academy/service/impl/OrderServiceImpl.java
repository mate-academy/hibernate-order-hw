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
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        List<Ticket> copiedTickets = new ArrayList<>();
        List<Ticket> shoppingCartTickets = shoppingCart.getTickets();
        copiedTickets = shoppingCartTickets;
        Order order = new Order();
        order.setTickets(copiedTickets);
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        Order addedOrder = orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return addedOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
