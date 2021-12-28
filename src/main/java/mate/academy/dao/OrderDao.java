package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order saveOrder(Order order);

    List<Order> getOrdersHistory(User user);
}
