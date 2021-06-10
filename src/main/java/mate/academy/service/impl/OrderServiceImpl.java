package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
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
        if (shoppingCart.getTickets().isEmpty()) {
            throw new RuntimeException("Your cart is empty, no orders to complete");
        }
        Order order = new Order();
        List<Ticket> ticketsCopy = shoppingCart.getTickets();
        order.setTickets(ticketsCopy);
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(shoppingCart.getTickets().get(0).getShowTime());
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderDao.add(order);
    }
    
    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
