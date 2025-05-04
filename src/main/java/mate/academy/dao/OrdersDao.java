package mate.academy.dao;

import java.util.List;
import mate.academy.model.Orders;
import mate.academy.model.User;

public interface OrdersDao {
    Orders add(Orders order);

    List<Orders> getByUser(User user);
}
