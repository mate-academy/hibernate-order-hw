package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

import java.util.List;

public interface OrderDao {
    Order add(Order order);

    List<Order> getByUser(User user);
}
