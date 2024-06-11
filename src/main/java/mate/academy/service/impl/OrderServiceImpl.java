package mate.academy.service.impl;

import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Inject
    private OrderDao orderDao;

    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        List<Ticket> ticketsCopy = new ArrayList<>(shoppingCart.getTickets());
        order.setTickets(ticketsCopy);
        order.setLocalDateTime(LocalDateTime.now());

        shoppingCart.getTickets().clear();

        try {
            orderDao.add(order);
            orderDao.updateShoppingCart(shoppingCart);
        } catch (DataProcessingException e) {
            throw new DataProcessingException("Can't complete order for shopping cart: "
                    + shoppingCart, e);
        }

        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
