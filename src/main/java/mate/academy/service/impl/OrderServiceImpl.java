package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        List<Ticket> remainedTickets = shoppingCart.getTickets().stream()
                .filter(ticket -> !order.getTickets().contains(ticket))
                .collect(Collectors.toList());
        shoppingCart.setTickets(remainedTickets);
        shoppingCartDao.update(shoppingCart);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersByUser(user);
    }
}
