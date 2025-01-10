package mate.academy.service.impl;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        List<Ticket> copyListTicket = new ArrayList<>(shoppingCart.getTickets());
        Order order = new Order();
        order.setTickets(copyListTicket);
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());

        orderDao.save(order);

        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
