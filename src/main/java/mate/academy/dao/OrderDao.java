package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

import java.util.List;

public interface OrderDao {
    List<Order> getOrdersByUser(User user);
    Order getUnfinishedOrderByUser(User user);
}
