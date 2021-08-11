package mate.academy;

import java.time.LocalDateTime;
import mate.academy.dao.CinemaHallDao;
import mate.academy.dao.MovieDao;
import mate.academy.dao.MovieSessionDao;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static final Injector injector = Injector.getInstance("mate.academy");
    public static final CinemaHallDao cinemaHallDao = (CinemaHallDao) injector
            .getInstance(CinemaHallDao.class);
    public static final CinemaHallService cinemaHallService = (CinemaHallService) injector
            .getInstance(CinemaHallService.class);
    public static final MovieDao movieDao = (MovieDao) injector.getInstance(MovieDao.class);
    public static final MovieService movieService = (MovieService) injector
            .getInstance(MovieService.class);
    public static final MovieSessionDao movieSessionDao = (MovieSessionDao) injector
            .getInstance(MovieSessionDao.class);
    public static final MovieSessionService movieSessionService = (MovieSessionService) injector
            .getInstance(MovieSessionService.class);
    public static final ShoppingCartDao shoppingCartDao = (ShoppingCartDao) injector
            .getInstance(ShoppingCartDao.class);
    public static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    public static final UserDao userDao = (UserDao) injector.getInstance(UserDao.class);
    public static final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    public static final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
    public static final OrderDao orderDao = (OrderDao) injector.getInstance(OrderDao.class);
    public static final OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);
    public static final AuthenticationService authenticationService =
            (AuthenticationService) injector
                    .getInstance(AuthenticationService.class);

    public static void main(String[] args) {
        cinemaHallService.add(new CinemaHall(100, "Big hall"));
        movieService.add(new Movie("1 + 1", "family film"));
        movieSessionService
                .add(new MovieSession(movieService.get(1L), cinemaHallService.get(1L), LocalDateTime
                        .of(2021, 10, 11, 10, 1)));

        User user = authenticationService.register("vova", "123");
        shoppingCartService.addSession(movieSessionService.get(1L), user);

        orderService.completeOrder(shoppingCartService.getByUser(user));
        System.out.println(shoppingCartService.getByUser(user).getTickets());

    }
}
