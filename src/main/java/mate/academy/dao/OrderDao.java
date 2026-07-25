package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;

public interface OrderDao {
    List<Order> getByUser(Long userId);

    Order add(Order order);
}
