package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order add(Order order);

    Optional<Order> get(Long id);

    Order getByUser(User user);

    List<Order> getAllOrdersByUser(User user);
}
