package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        List<Ticket> shoppingCartTickets = shoppingCart.getTickets();
        List<Ticket> shoppingCartTicketsCopy = new ArrayList<>(shoppingCartTickets);
        Collections.copy(shoppingCartTicketsCopy, shoppingCartTickets);
        newOrder.setTickets(shoppingCartTicketsCopy);
        orderDao.add(newOrder);
        return newOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
