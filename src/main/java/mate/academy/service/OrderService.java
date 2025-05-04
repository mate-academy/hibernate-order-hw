package mate.academy.service;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderService {
    Order add(Order order);

    List<Order> getOrdersHistory(User user);
}
