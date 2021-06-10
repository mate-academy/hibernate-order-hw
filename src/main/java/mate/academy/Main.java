package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private static AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);
    private static ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

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

        User bob = authenticationService.register("bob@gmail.com", "bobp@ssW");
        User alice = authenticationService.register("alice@gmail.com", "al1c3p@ssW");
        System.out.println("_____________________________________________________");
        System.out.println(bob);
        System.out.println(alice);

        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, alice);

        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(bob);
        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(alice);
        System.out.println("_____________________________________________________");
        System.out.println(bobShoppingCart);
        System.out.println(aliceShoppingCart);

        System.out.println("_____________________________________________________");
        System.out.println(orderService.completeOrder(bobShoppingCart));
        System.out.println(orderService.getOrdersHistory(bob));
        System.out.println(bobShoppingCart);
        System.out.println(orderService.completeOrder(aliceShoppingCart));
        System.out.println(orderService.getOrdersHistory(alice));
        System.out.println(aliceShoppingCart);
    }
}
