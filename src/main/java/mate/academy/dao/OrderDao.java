package mate.academy.dao;

import java.util.Optional;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;

@Dao
public interface OrderDao {
    Optional<Order> getByUser(User user);

    Optional<Order> get(Long id);

    Order add(Order session);
}
