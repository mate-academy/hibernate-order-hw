package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Dao;
import mate.academy.model.Order;
import mate.academy.model.User;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        return null;
    }

    @Override
    public List<Order> getByUser(User user) {
        return null;
    }
}
