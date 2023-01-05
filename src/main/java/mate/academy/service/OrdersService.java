package mate.academy.service;

import java.util.List;
import mate.academy.model.Orders;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface OrdersService {
    Orders completeOrder(ShoppingCart shoppingCart);

    List<Orders> getOrdersHistory(User user);
}
