package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

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

        CinemaHallService cinemaHallService = (CinemaHallService) injector.getInstance(
                CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(firstCinemaHall);
        todayMovieSession.setMovie(fastAndFurious);
        todayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService) injector.getInstance(
                MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(todayMovieSession);

        System.out.println(movieSessionService.get(todayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = (AuthenticationService) injector.getInstance(
                AuthenticationService.class);
        User loginBob;

        try {
            authenticationService.register("bob@gmail.com", "password");
            loginBob = authenticationService.login("bob@gmail.com", "password");
        } catch (RegistrationException e) {
            logger.log(Level.SEVERE, "Error during user registration: ", e);
            throw new RuntimeException("Failed to register a new user."
                    + " Please check your email or password.", e);
        } catch (AuthenticationException e) {
            logger.log(Level.SEVERE, "Error during user authentication: ", e);
            throw new RuntimeException("Failed to login in account, "
                    + "email or password is incorrect.");
        }

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector.getInstance(
                ShoppingCartService.class);
        shoppingCartService.addSession(todayMovieSession, loginBob);
        shoppingCartService.addSession(tomorrowMovieSession, loginBob);
        System.out.println(shoppingCartService.getByUser(loginBob));

        //Testing the functionality of OrderDao and OrderService.
        OrderService orderService = (OrderService) injector.getInstance(
                OrderService.class);
        //1. Completing a new order.
        orderService.completeOrder(shoppingCartService.getByUser(loginBob));
        //2. Retrieving order history for the user.
        System.out.println(orderService.getOrdersHistory(loginBob));
    }
}
