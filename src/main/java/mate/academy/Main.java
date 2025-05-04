package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static final Injector injector = Injector.getInstance("mate.academy");
    public static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    public static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    public static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    public static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    public static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);
    public static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);

    public static void main(String[] args) throws RegistrationException {

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

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

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User user = authenticationService.register("bob@ukr.net", "bober");
        shoppingCartService.addSession(tomorrowMovieSession, user);
        shoppingCartService.addSession(tomorrowMovieSession, user);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        System.out.println(shoppingCart);

        System.out.println(orderService.completeOrder(shoppingCart));
        System.out.println(orderService.getOrdersHistory(user));
    }
}
