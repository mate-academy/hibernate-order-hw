package mate.academy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private static final TicketDao ticketDao =
            (TicketDao) injector.getInstance(TicketDao.class);

    public static void main(String[] args) {
        CinemaHall cinemaHall1 = new CinemaHall();
        cinemaHall1.setCapacity(100);
        cinemaHall1.setDescription("Hall 1");

        CinemaHall cinemaHall2 = new CinemaHall();
        cinemaHall2.setCapacity(200);
        cinemaHall2.setDescription("Hall 2");

        cinemaHallService.add(cinemaHall1);
        cinemaHallService.add(cinemaHall2);

        Movie movie1 = new Movie("Cool guy 1");
        Movie movie2 = new Movie("Cool guy 2");

        movieService.add(movie1);
        movieService.add(movie2);

        MovieSession movieSession1 = new MovieSession();
        movieSession1.setMovie(movie1);
        movieSession1.setCinemaHall(cinemaHall1);
        movieSession1.setShowTime(LocalDateTime.now());

        MovieSession movieSession2 = new MovieSession();
        movieSession2.setMovie(movie2);
        movieSession2.setCinemaHall(cinemaHall2);
        movieSession2.setShowTime(LocalDateTime.now().plusDays(1));

        movieSessionService.add(movieSession1);
        movieSessionService.add(movieSession2);

        User user = new User();
        user.setEmail("derek@gmail.com");
        user.setPassword("1234");

        userService.add(user);

        Ticket ticket1 = new Ticket();
        ticket1.setMovieSession(movieSession1);
        ticket1.setUser(user);

        Ticket ticket2 = new Ticket();
        ticket2.setMovieSession(movieSession2);
        ticket2.setUser(user);

        ticketDao.add(ticket1);
        ticketDao.add(ticket2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>(List.of(ticket1, ticket2)));

        Order order = orderService.completeOrder(shoppingCart);
        System.out.println("Completed order: " + order);

        List<Order> orders = orderService.getOrdersHistory(user);
        orders.forEach(System.out::println);
    }
}
