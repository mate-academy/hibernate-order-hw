package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Optional<Order> getByUser(User user);

    Order add(Order order);
}
