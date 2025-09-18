package mate.academy.service;

import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

import java.util.List;

public interface OrderService {
    Order completeOrder(ShoppingCart shoppingCart);

    List<Order> getOrdersHistory(User user);
}
