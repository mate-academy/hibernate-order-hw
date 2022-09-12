package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface OrderDao {
    Optional<Order> completeOrder(ShoppingCart shoppingCart);

    List<Order> getOrdersHistory(User user);
}
