package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrdersDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Orders;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Inject
    private OrdersDao ordersDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Orders completeOrder(ShoppingCart shoppingCart) {
        Orders orders = new Orders(shoppingCart.getTickets(),
                LocalDateTime.now(),
                shoppingCart.getUser());
        return ordersDao.add(orders);
    }

    @Override
    public List<Orders> getOrdersHistory(User user) {
        return ordersDao.getByUser(user);
    }
}
