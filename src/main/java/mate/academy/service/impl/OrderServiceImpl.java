package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.*;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setTickets(shoppingCart.getTickets());
        order.setUser(shoppingCart.getUser());
        List<Ticket> tickets = shoppingCart.getTickets();
        LocalDateTime lastDate = tickets.stream()
                .map(Ticket::getMovieSession)
                .map(MovieSession::getShowTime)
                .max(Comparator.comparing(LocalDateTime::toLocalDate))
                .orElse(null);
        order.setOrderDate(lastDate);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getOrdersHistory(user);
    }
}
