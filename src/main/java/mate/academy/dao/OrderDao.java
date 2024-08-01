package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    List<Order> getByUser(User user);
    Order add(Order order);


}
