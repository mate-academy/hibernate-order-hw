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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    private static final String WRONG_BOB_PASSWORD = "afasfqf";

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

        User bob = new User();
        bob.setEmail("bob12@gmail.com");
        bob.setPassword("qwerty11");

        User alice = new User();
        alice.setEmail("alice7@gmail.com");
        alice.setPassword("123456");

        try {
            authenticationService.register(bob.getEmail(), bob.getPassword());
            authenticationService.register(alice.getEmail(), alice.getPassword());
            // Error case
            authenticationService.register(bob.getEmail(), bob.getPassword());
        } catch (RegistrationException e) {
            System.out.println("Registration failed! " + e.getMessage());
        }

        try {
            authenticationService.login(bob.getEmail(), bob.getPassword());
            authenticationService.login(alice.getEmail(), alice.getPassword());
            // Error case
            authenticationService.login(bob.getEmail(), WRONG_BOB_PASSWORD);
        } catch (AuthenticationException e) {
            System.out.println("Login failed! " + e.getMessage());
        }

        // ShoppingCartService testing
        User bobFromDb = userService.findByEmail("bob12@gmail.com").get();
        User aliceFromDb = userService.findByEmail("alice7@gmail.com").get();
        System.out.println(bobFromDb);
        System.out.println(aliceFromDb);

        shoppingCartService.addSession(tomorrowMovieSession, bobFromDb);
        shoppingCartService.addSession(yesterdayMovieSession, aliceFromDb);
        System.out.println(shoppingCartService.getByUser(bobFromDb));
        System.out.println(shoppingCartService.getByUser(aliceFromDb));

        shoppingCartService.clearShoppingCart(shoppingCartService.getByUser(bobFromDb));
        System.out.println(shoppingCartService.getByUser(bobFromDb));

        // OrderService testing
        System.out.println(orderService.completeOrder(shoppingCartService.getByUser(aliceFromDb)));
        //Error case as no tickets in bob's shopping cart after clearing (line:114)
        //System.out.println(orderService.completeOrder(shoppingCartService.getByUser(bobFromDb)));

        orderService.getOrdersHistory(aliceFromDb).forEach(System.out::println);
    }
}
