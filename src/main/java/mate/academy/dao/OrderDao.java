package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    public Order add(Order order);

    public List<Order> getAllByUser(User user);
}
