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
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println("Getting 1st movie by ID");
        System.out.println(movieService.get(fastAndFurious.getId()));
        System.out.println(System.lineSeparator() + "Getting all movies list");
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(System.lineSeparator() + "Getting all cinema halls list");
        System.out.println(cinemaHallService.getAll());
        System.out.println(System.lineSeparator() + "Getting 1st cinema hall by ID");
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(System.lineSeparator() + "Getting yesterday movie sessions");
        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(System.lineSeparator()
                + "Getting today movie sessions (zero result assumed)");
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        try {
            authenticationService.register("user1@mail.com", "1234");
        } catch (RegistrationException e) {
            System.out.println("User1 registration failed " + e);
        }
        try {
            authenticationService.register("user2@mail.com", "4321");
        } catch (RegistrationException e) {
            System.out.println("User2 registration failed " + e);
        }
        System.out.println(System.lineSeparator() + "Login test");
        try {
            User user1 = authenticationService.login("user1@mail.com", "1234");
            System.out.println(user1);
        } catch (AuthenticationException e) {
            System.out.println("user1 login failed" + e);
        }
        try {
            User user2 = authenticationService.login("user2@mail.com", "4321");
            System.out.println(user2);
        } catch (AuthenticationException e) {
            System.out.println("user2 login failed" + e);
        }

        try {
            authenticationService.login("user1@mail.com", "****");
            System.out.println("user with wrong password authenticated, something went wrong");
        } catch (AuthenticationException e) {
            System.out.println(System.lineSeparator()
                    + "INTENTIONAL EXCEPTION CHECK ON WRONG PASSWORD, "
                    + "RESULT OK: Login failed " + e);
        }

        try {
            authenticationService.register("user2@mail.com", "1234");
            System.out.println("user with same email registered, something is wrong");
        } catch (RegistrationException e) {
            System.out.println(System.lineSeparator()
                    + "INTENTIONAL EXCEPTION CHECK ON REGISTRATION WITH SAME EMAIL, "
                    + "RESULT OK: " + e);
        }

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);

        try {
            User user1 = authenticationService.login("user1@mail.com", "1234");
            MovieSession movieSession1 = movieSessionService
                    .findAvailableSessions(1L, LocalDate.now().minusDays(1)).get(0);
            MovieSession movieSession2 = movieSessionService
                    .findAvailableSessions(1L, LocalDate.now().plusDays(1)).get(0);
            shoppingCartService.addSession(movieSession1, user1);
            shoppingCartService.addSession(movieSession2, user1);
            ShoppingCart shoppingCart = shoppingCartService.getByUser(user1);
            System.out.println(System.lineSeparator() + "Checking user1 cart");
            System.out.println(shoppingCart);
            System.out.println(System.lineSeparator() + "Checking order created");
            System.out.println(orderService.completeOrder(shoppingCart));
            System.out.println(System.lineSeparator() + "Cart after completing order");
            System.out.println(shoppingCartService.getByUser(shoppingCart.getUser()));
            System.out.println(System.lineSeparator() + "Getting orders history");
            orderService.getOrdersHistory(user1).forEach(System.out::println);

        } catch (AuthenticationException e) {
            System.out.println("user1 login failed" + e);
        }
    }
}
