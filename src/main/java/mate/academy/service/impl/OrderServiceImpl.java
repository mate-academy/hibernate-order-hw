package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
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
        Order currentOrder = new Order();
        currentOrder.setOrderDate(LocalDateTime.now());
        currentOrder.setUser(shoppingCart.getUser());
        currentOrder.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        shoppingCartService.clearShoppingCart(shoppingCart);
        orderDao.add(currentOrder);
        return currentOrder;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> orderHistory = orderDao.findByUser(user);
        if (orderHistory.isEmpty()) {
            System.out.println("Orders history is empty");
        }
        return orderHistory;
    }
}
