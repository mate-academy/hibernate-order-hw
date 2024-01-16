package mate.academy.dao;

import java.util.List;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;

@Dao
public interface OrderDao {
    Order add(Order order);

    List<Order> getByUser(User user);
}
