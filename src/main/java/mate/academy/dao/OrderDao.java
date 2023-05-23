package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order addOrder(Order order);

    List<Order> getOrdersByUser(User user);
}
