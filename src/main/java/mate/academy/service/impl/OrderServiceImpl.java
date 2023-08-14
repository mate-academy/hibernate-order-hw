package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Order createOrder(ShoppingCart shoppingCart) {
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        newOrder.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        newOrder.setOrderDate(LocalDateTime.now());
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
        return orderDao.add(newOrder);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }
}
