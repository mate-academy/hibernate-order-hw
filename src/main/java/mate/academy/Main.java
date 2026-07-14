package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import mate.academy.dao.OrderDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.OrderDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;

public class Main {
    public static void main(String[] args) {
        OrderDao orderDao = new OrderDaoImpl();
        UserDao userDao = new UserDaoImpl();
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = new User();
        user.setEmail("artem@gmail.com");
        userDao.add(user);

        shoppingCart.setUser(user);
        orderDao.addOrder(shoppingCart);

        List<Order> allOrders = orderDao.getByUser(user);
        for (Order order : allOrders) {
            System.out.println(order);
        }

    }
}
