package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.OrderDao;
import mate.academy.model.Order;
import mate.academy.model.User;

public class OrderDaoImpl implements OrderDao {
    @Override
    public Optional<Order> getByUser(User user) {

        return Optional.empty();
    }

    @Override
    public Optional<Order> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Order add(Order session) {
        return null;
    }
}
