package mate.academy.service.impl;

import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    @Transactional
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = orderDao.getUnfinishedOrderByUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersByUser(user);
    }
}
