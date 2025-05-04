package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order add(Order order) {
        return orderDao.add(order);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Order getById(Long id) {
        return orderDao.getById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't get an order by id " + id));
    }

    @Override
    public Order getByUser(User user) {
        return orderDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't get an order by user " + user));
    }

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        order.setUser(shoppingCart.getUser());
        order = orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getAllByUser(user);
    }
}
