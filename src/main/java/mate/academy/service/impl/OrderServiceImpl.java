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
        if (shoppingCart == null || shoppingCart.getTickets().isEmpty()) {
            throw new DataProcessingException("Can't complete order"
                    + " - no tickets in the shopping cart! " + shoppingCart);
        }
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        newOrder.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        newOrder.setOrderDate(LocalDateTime.now());
        Order completedOrder = orderDao.add(newOrder);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return completedOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
