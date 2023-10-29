package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order add(Order order);

    Order getByUser(User user);
}
