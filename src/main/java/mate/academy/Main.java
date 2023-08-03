package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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

public class Main {
    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        final Injector injector = Injector.getInstance("mate");
        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

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

        MovieSession dayAfterTomorrowMovieSession = new MovieSession();
        dayAfterTomorrowMovieSession.setCinemaHall(secondCinemaHall);
        dayAfterTomorrowMovieSession.setMovie(fastAndFurious);
        dayAfterTomorrowMovieSession.setShowTime(LocalDateTime.now().minusDays(2L));

        final MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        final MovieSession tomorrowMovieSessionFromDb =
                movieSessionService.add(tomorrowMovieSession);
        final MovieSession dayAfterTomorrowMovieSessionFromDb =
                movieSessionService.add(dayAfterTomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User user = new User();
        user.setEmail("@userEmail");
        user.setPassword("qwerty");
        User registeredUser = authenticationService.register(user.getEmail(), user.getPassword());
        System.out.println(authenticationService.login("@userEmail", "qwerty"));

        System.out.println(shoppingCartService.getByUser(registeredUser));

        shoppingCartService.addSession(tomorrowMovieSessionFromDb, registeredUser);

        orderService.completeOrder(shoppingCartService.getByUser(registeredUser));

        shoppingCartService.addSession(dayAfterTomorrowMovieSessionFromDb, registeredUser);

        orderService.completeOrder(shoppingCartService.getByUser(registeredUser));

        for (Order order : orderService.getOrdersHistory(registeredUser)) {
            System.out.println(order);
        }
    }
}
