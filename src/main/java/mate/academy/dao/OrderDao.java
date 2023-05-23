package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao extends GenericDao<Order> {
    List<Order> getByUser(User user);
}
