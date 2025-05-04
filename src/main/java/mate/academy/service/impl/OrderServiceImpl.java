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

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        List<Ticket> ticketsCopy = new ArrayList<>(shoppingCart.getTickets());

        Order order = new Order();
        order.setTickets(ticketsCopy);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(shoppingCart.getUser());

        shoppingCart.getTickets().clear();

        return orderDao.addOrder(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
