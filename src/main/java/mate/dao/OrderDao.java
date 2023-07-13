package mate.dao;

import java.util.List;
import mate.model.Order;
import mate.model.User;

public interface OrderDao {
    Order add(Order order);

    List<Order> getOrdersHistory(User user);
}
