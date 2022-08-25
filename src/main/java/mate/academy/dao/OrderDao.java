package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

import java.util.List;

public interface OrderDao {
    Order add(Order order);

    Order getByUser(User user);

    List<Order> getAll();

    List<Order> getOrdersHistory(User user);

    void update(Order order);
}
