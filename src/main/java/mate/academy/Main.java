package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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

    private static final MovieService movieService = (MovieService) injector
            .getInstance(MovieService.class);

    private static final CinemaHallService cinemaHallService = (CinemaHallService) injector
            .getInstance(CinemaHallService.class);

    private static final MovieSessionService movieSessionService = (MovieSessionService) injector
            .getInstance(MovieSessionService.class);

    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);

    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    public static void main(String[] args) throws AuthenticationException {
        injectUsers();

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

        injectShoppingCartsAndOrders();
    }

    private static void injectUsers() {
        authenticationService.register("bob@gmail.com", "qwerty");
        authenticationService.register("alice@gmail.com", "123456");
        authenticationService.register("john@gmail.com", "johnny");
    }

    private static void injectShoppingCartsAndOrders() throws AuthenticationException {
        User bob = authenticationService.login("bob@gmail.com", "qwerty");
        MovieSession movieSession1 = movieSessionService.get(1L);
        MovieSession movieSession2 = movieSessionService.get(2L);
        shoppingCartService.addSession(movieSession1, bob);
        shoppingCartService.addSession(movieSession1, bob);
        shoppingCartService.addSession(movieSession2, bob);

        User alice = authenticationService.login("alice@gmail.com", "123456");
        shoppingCartService.addSession(movieSession2, alice);

        ShoppingCart shoppingCartBob = shoppingCartService.getByUser(bob);
        System.out.println("===================");
        System.out.println(shoppingCartBob);

        ShoppingCart shoppingCartAlice = shoppingCartService.getByUser(alice);
        System.out.println("===================");
        System.out.println(shoppingCartAlice);

        Order bobOrder = orderService.completeOrder(shoppingCartBob);
        System.out.println("===================");
        System.out.println(bobOrder);
        System.out.println(shoppingCartService.getByUser(bob));
    }
}
