package mate.academy.dao.impl;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;
public interface OrderDao {
    Order createOrder(Order order);

    List<Order> getOrdersHistory(User user);
}
