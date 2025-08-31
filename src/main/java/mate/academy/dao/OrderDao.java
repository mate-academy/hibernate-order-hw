package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;

public interface OrderDao {
    Order add(Order order);

    Order getById(Long id);

    List<Order> getByUser(Long userId);
}
