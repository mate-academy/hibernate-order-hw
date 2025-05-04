package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Optional<Order> get(Long id);

    Order add(Order order);

    List<Order> getByUser(User user);
}
