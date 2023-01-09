package mate.service;

import java.util.List;
import mate.model.Order;
import mate.model.ShoppingCart;
import mate.model.User;

public interface OrderService {

    Order completeOrder(ShoppingCart shoppingCart);

    List<Order> getOrdersHistory(User user);
}
