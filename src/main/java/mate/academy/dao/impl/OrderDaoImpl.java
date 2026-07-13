package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public class OrderDaoImpl implements OrderDao {
    @Override
    public List<Order> getByUser(User user) {
        return List.of();
    }

    @Override
    public Order addOrder(ShoppingCart shoppingCart) {
        return new Order();
    }
}
