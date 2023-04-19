package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
//        orderDao.completeOrder(shoppingCart);
        /*
         * When passing tickets from shoppingCart to order — pass the copy of this
         * list(you can use ArrayList constructor for this); The reason is that
         * Hibernate will automatically delete these tickets from shoppingCart(in DB)
         * after noticing them in our order object. Let’s do this manually using clear()
         * method from ShoppingCartService after the order with tickets has been
         * created.
         */
        return null;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
