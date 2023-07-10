package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Optional<List<Order>> getByUser(User user);

    Optional<List<Order>> get(Long id);

    Order add(Order order);
}
