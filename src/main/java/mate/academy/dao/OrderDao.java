package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;

public interface OrderDao {
    Order add(Order order);

    List<Order> getByUserId(Long userId);
}
