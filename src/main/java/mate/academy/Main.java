package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);

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
                (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService =
                (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);
        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("bobspassword");
        User registeredBob = null;

        try {
            registeredBob = authenticationService.register(bob.getEmail(), bob.getPassword());
            System.out.println("Successfully registered: " + registeredBob);
            User loggedBob = authenticationService.login(bob.getEmail(), bob.getPassword());
            System.out.println("Successfully logged in: " + loggedBob);
        } catch (RegistrationException | AuthenticationException e) {
            throw new RuntimeException(e);
        }

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(tomorrowMovieSession, registeredBob);
        ShoppingCart bobsShoppingCart = shoppingCartService.getByUser(registeredBob);

        OrderService orderService =
                (OrderService) INJECTOR.getInstance(OrderService.class);
        orderService.completeOrder(bobsShoppingCart);
        shoppingCartService.clearShoppingCart(bobsShoppingCart);

        // Adding other shopping cart and order to get at least 2 in history to verify getAll()
        shoppingCartService.addSession(yesterdayMovieSession, registeredBob);
        ShoppingCart bobsShoppingCartTwo = shoppingCartService.getByUser(registeredBob);
        orderService.completeOrder(bobsShoppingCartTwo);
        shoppingCartService.clearShoppingCart(bobsShoppingCartTwo);

        List<Order> ordersHistory = orderService.getOrdersHistory(registeredBob);
        ordersHistory.forEach(System.out::println);
    }
}
