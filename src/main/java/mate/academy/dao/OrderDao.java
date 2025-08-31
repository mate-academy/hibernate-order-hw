package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

import java.util.List;

public interface OrderDao {
    Order add(Order order);

    Order getById(Long id);

    List<Order> getByUser(Long userId);
}
