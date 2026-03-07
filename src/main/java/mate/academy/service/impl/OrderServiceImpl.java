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
import mate.academy.service.OrderSevice;

@Service
public class OrderServiceImpl implements OrderSevice {
    @Inject
    private OrderDao orderDao;

    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        order.setUser(shoppingCart.getUser());

        orderDao.add(order);

        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);

        return order;
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        return orderDao.getByUser(user);
    }
}
