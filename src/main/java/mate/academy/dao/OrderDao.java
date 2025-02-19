package mate.academy.dao;

import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order getByUser(User user);

    Order completeOrder(Order order);
}
