package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static ShoppingCartService cartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        Movie fastAndFurious = new Movie("Fast and Furious",
                "An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall(100, "first hall with capacity 100");
        CinemaHall secondCinemaHall = new CinemaHall(200, "second hall with capacity 200");
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);
        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession(fastAndFurious,
                firstCinemaHall, LocalDateTime.now().plusDays(1L));
        MovieSession yesterdayMovieSession = new MovieSession(fastAndFurious,
                firstCinemaHall, LocalDateTime.now().minusDays(1L));
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);
        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User register = authenticationService.register("bob@gmail.com", "12345");
        User login = authenticationService.login("bob@gmail.com", "12345");
        System.out.println("User " + register
                + "register right now and login in system with next parameters" + login);
        cartService.addSession(tomorrowMovieSession, login);
        cartService.addSession(yesterdayMovieSession, login);
        orderService.completeOrder(cartService.getByUser(login));
        System.out.println("Orders: " + orderService.getOrdersHistory(login));
    }
}
