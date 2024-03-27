package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        List<Ticket> tickets = new ArrayList<>(shoppingCart.getTickets());
        newOrder.setTickets(tickets);
        LocalDateTime earliestDateOfMovieSession = findEarliestDateOfMovieSession(tickets);
        newOrder.setOrderDate(earliestDateOfMovieSession.minusDays(3));
        return orderDao.add(newOrder);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getByUser(user);
    }

    private LocalDateTime findEarliestDateOfMovieSession(List<Ticket> tickets) {
        LocalDateTime[] arrShowTimes = new LocalDateTime[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            arrShowTimes[i] = tickets.get(i).getMovieSession().getShowTime();
        }
        LocalDateTime minDate = arrShowTimes[0];
        for (LocalDateTime someDate : arrShowTimes) {
            if (someDate.compareTo(minDate) < 0) {
                minDate = someDate;
            }
        }
        return minDate;
    }
}
