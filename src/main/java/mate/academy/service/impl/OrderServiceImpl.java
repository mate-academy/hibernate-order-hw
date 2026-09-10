package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private OrderDao orderDao;

    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        try {
            List<Ticket> ticketsFromOrder = new ArrayList<>(shoppingCart.getTickets());
            order.setTickets(ticketsFromOrder);
            order.setOrderDate(LocalDateTime.now());
            order.setUser(shoppingCart.getUser());
            Order addedOrder = orderDao.add(order);
            shoppingCartService.clearShoppingCart(shoppingCart);
            return addedOrder;
        } catch (Exception e) {
            throw new DataProcessingException("Can't complete order from shopping cart: "
                    + shoppingCart, e);
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
