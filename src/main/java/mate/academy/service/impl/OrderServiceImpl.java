package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.exception.DataProcessingException;
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
    private static OrderDao orderDao;
    @Inject
    private static ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new DataProcessingException(
                    "Internal error, shopping cart object is null");
        }
        List<Ticket> tickets = shoppingCart.getTickets();
        if (tickets.isEmpty()) {
            throw new DataProcessingException(
                    "Internal error, there are no tickets in the shopping cart");
        }
        Order toPlace = new Order();
        toPlace.setUser(shoppingCart.getUser());
        toPlace.setTickets(tickets);
        toPlace.setDateTime(LocalDateTime.now());
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderDao.add(toPlace);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
