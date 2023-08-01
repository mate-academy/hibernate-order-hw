package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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
    private static final Injector inject = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) inject.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) inject.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) inject.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) inject.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) inject.getInstance(ShoppingCartService.class);
    private static final OrderService orderService =
            (OrderService) inject.getInstance(OrderService.class);

    public static void main(String[] args) {

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

        User user = null;
        try {
            user = authenticationService.register("batman@gmail.com", "5432vcbfg43fgbyyn42");
            System.out.println("User registration successful [ " + user + " ]");
        } catch (RegistrationException e) {
            throw new RuntimeException("Registration error");
        }

        try {
            System.out.println("Login : "
                    + authenticationService.login(user.getEmail(), "5432vcbfg43fgbyyn42"));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email or password is incorrect (", e);
        }
        shoppingCartService.addSession(tomorrowMovieSession, user);
        ShoppingCart shoppingCartServiceByUser = shoppingCartService.getByUser(user);

        orderService.getOrdersHistory(user).forEach(System.out::println);
        System.out.println(orderService.completeOrder(shoppingCartServiceByUser));

    }
}
