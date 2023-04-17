package mate.academy.service.impl;

import java.time.LocalDateTime;
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

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        User user = shoppingCart.getUser();
        List<Ticket> tickets = shoppingCartDao
                .getByUser(user).getTickets();
        LocalDateTime orderDate = LocalDateTime.now();
        Order order = new Order(user, orderDate, tickets);
        order = orderDao.add(order);
        tickets.clear();
        shoppingCart.setTickets(tickets);
        shoppingCartDao.update(shoppingCart);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
