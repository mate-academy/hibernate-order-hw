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
    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        Injector injector = Injector.getInstance("mate.academy");

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

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(firstCinemaHall);
        movieSession.setMovie(fastAndFurious);
        movieSession.setShowTime(LocalDateTime.now());

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        MovieSession savedMovieSession = movieSessionService.add(movieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(savedMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        User registeredBob =
                authenticationService.register("bob@mail", "bob123bob");
        User registeredAlice =
                authenticationService.register("alice@mail", "alice123alice");

        User loggedBob = authenticationService
                .login("bob@mail", "bob123bob");
        User loggedAlice = authenticationService
                .login("alice@mail", "alice123alice");

        System.out.println(loggedBob);
        System.out.println(loggedAlice);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(savedMovieSession, loggedBob);
        shoppingCartService.addSession(savedMovieSession, loggedAlice);

        ShoppingCart bobsShoppingCart = shoppingCartService.getByUser(loggedBob);
        ShoppingCart alicesShoppingCart = shoppingCartService.getByUser(loggedAlice);

        shoppingCartService.clearShoppingCart(alicesShoppingCart);
        ShoppingCart alicesShoppingCartWithoutTickets = shoppingCartService.getByUser(loggedAlice);

        System.out.println(bobsShoppingCart);
        System.out.println(alicesShoppingCartWithoutTickets);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        orderService.completeOrder(bobsShoppingCart);
        System.out.println(orderService.getOrdersHistory(loggedBob));
        try {
            orderService.completeOrder(alicesShoppingCartWithoutTickets);
        } catch (RuntimeException e) {
            System.out.println("Method completedOrder() with empty shopping cart works correct");
        }
    }
}
