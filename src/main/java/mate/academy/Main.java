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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final UserService userService
            = (UserService) injector.getInstance(UserService.class);
    private static final OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);
    private static final AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);

    public static void main(String[] args) throws AuthenticationException, RegistrationException {
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

        User firstUser;
        if (userService.findByEmail("sss@i.com").isPresent()) {
            firstUser = authenticationService.login("sss@i.com", "12345");
        } else {
            firstUser = authenticationService.register("sss@i.com", "12345");
        }

        System.out.println("\nFind shopping cart ...");
        ShoppingCart firstShoppingCart = shoppingCartService.getByUser(firstUser);
        System.out.println(firstShoppingCart);
        System.out.println("Shopping cart found");

        System.out.println("\nAdd ticket to shopping cart ...");
        shoppingCartService.addSession(tomorrowMovieSession, firstUser);
        System.out.println("Ticket 1 added to shopping cart");
        shoppingCartService.addSession(yesterdayMovieSession, firstUser);
        System.out.println("Ticket 2 added to shopping cart");
        System.out.println(shoppingCartService.getByUser(firstUser));
        System.out.println("Ticket added to shopping cart");

        firstShoppingCart = shoppingCartService.getByUser(firstUser);

        System.out.println("\nAdd tickets to order ...");
        orderService.completeOrder(firstShoppingCart);
        System.out.println("Order 1 added");

        System.out.println("\nGet order history ...");
        orderService.getOrdersHistory(firstUser);
    }
}
