package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface OrderDao {

    Order add(ShoppingCart shoppingCart);

    List<Order> getOrdersHistory(User user);
}
