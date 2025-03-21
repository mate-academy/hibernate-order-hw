package mate.academy.service.impl;

import java.time.LocalDateTime;
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
        if (shoppingCart == null) {
            throw new DataProcessingException("Shopping cart cannot be null", 
                    new IllegalArgumentException());
        }
        if (shoppingCart.getTickets() == null || shoppingCart.getTickets().isEmpty()) {
            throw new DataProcessingException("Cannot complete order with empty shopping cart", 
                    new IllegalArgumentException());
        }
        
        Order order = new Order();
        order.setTickets(List.copyOf(shoppingCart.getTickets()));
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        if (user == null) {
            throw new DataProcessingException("User cannot be null when retrieving order history", 
                    new IllegalArgumentException());
        }
        return orderDao.getByUser(user);
    }
}
