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
import mate.academy.service.ShoppingCartService;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(new ArrayList<>(shoppingCart.getTickets()));
        order.setUser(shoppingCart.getUser());
        shoppingCartService.clearShoppingCart(shoppingCart);
        orderDao.add(order);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        List<Order> orderList = orderDao.getByUser(user);
        for (Order order : orderList) {
            System.out.println("Order #" + order.getId() + " : " + order.getOrderDate()
                    + System.lineSeparator());
            for (Ticket ticket : order.getTickets()) {
                System.out.println("Ticket #" + ticket.getId() + " : " + System.lineSeparator());
                System.out.println("Movie: " + ticket.getMovieSession().getMovie().getTitle()
                        + System.lineSeparator() + "CinemaHall: "
                        + ticket.getMovieSession().getCinemaHall().getDescription());
            }
        }
        return orderList;
    }
}
