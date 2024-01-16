package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> getByUser(User user);

    Order add(Order order);
}
