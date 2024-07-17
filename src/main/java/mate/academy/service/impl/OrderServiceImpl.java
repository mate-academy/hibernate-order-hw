package mate.academy.service.impl;

import mate.academy.dao.OrderDao;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        return orderDao.add(new Order(shoppingCart.getTickets(), LocalDateTime.now(),
                shoppingCart.getUser()));
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getHistoryByUser(user);
    }
}
