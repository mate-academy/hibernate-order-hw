package mate.academy;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));
        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        User bob = null;
        User alis = null;
        try {
            bob = authenticationService.register("bob@gmail.com", "bob1234");
            alis = authenticationService.register("alis@gmail.com", "alis1234");
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }

        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        System.out.println(shoppingCartService.getByUser(bob));
        orderService.completeOrder(shoppingCartService.getByUser(bob));
        System.out.println(shoppingCartService.getByUser(bob));
        shoppingCartService.clearShoppingCart(shoppingCartService.getByUser(bob));
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        orderService.completeOrder(shoppingCartService.getByUser(bob));
        List<Order> ordersHistory = orderService.getOrdersHistory(bob);
        ordersHistory.forEach(System.out::println);
    }
}
