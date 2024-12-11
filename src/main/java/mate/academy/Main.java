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
        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);

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

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        // Register a new user
        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        String email = "test@example.com";
        String password = "securePassword";

        try {
            User registeredUser = authenticationService.register(email, password);
            System.out.println("User registered: " + registeredUser);
        } catch (RegistrationException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }

        // Login with correct email and password (retrieve the registered user)
        User user = null;
        try {
            user = authenticationService.login(email, password);
            System.out.println("User logged in: " + user);
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }

        if (user != null) {
            // Add sessions to shopping cart
            ShoppingCartService shoppingCartService =
                    (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

            shoppingCartService.addSession(yesterdayMovieSession, user);
            System.out.println("Shopping cart before completing order:");
            System.out.println(shoppingCartService.getByUser(user));

            // OrderService Tests
            OrderService orderService =
                    (OrderService) injector.getInstance(OrderService.class);

            System.out.println("Create an order with an empty cart for "
                    + user.getEmail());
            ShoppingCart emptyCart = shoppingCartService.getByUser(user);
            Order emptyOrder = orderService.completeOrder(emptyCart);
            System.out.println("Created order with an empty cart: " + emptyOrder);

            System.out.println("Add items and create an order");
            shoppingCartService.addSession(tomorrowMovieSession, user);
            shoppingCartService.addSession(yesterdayMovieSession, user);
            ShoppingCart cartWithItems = shoppingCartService.getByUser(user);
            System.out.println("Cart before placing an order: " + cartWithItems);

            Order firstOrder = orderService.completeOrder(cartWithItems);
            System.out.println("First order: " + firstOrder);

            System.out.println("Create a second order");
            shoppingCartService.addSession(tomorrowMovieSession, user);
            ShoppingCart newCartWithItems = shoppingCartService.getByUser(user);
            Order secondOrder = orderService.completeOrder(newCartWithItems);
            System.out.println("Second order: " + secondOrder);

            System.out.println("Order history for " + user.getEmail() + ":");
            orderService.getOrdersHistory(user).forEach(System.out::println);
        }
    }
}
