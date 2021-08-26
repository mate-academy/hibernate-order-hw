package mate.academy.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class GlobalTest extends TicketAppTest {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService = (MovieService) injector
            .getInstance(MovieService.class);
    private static CinemaHallService cinemaHallService = (CinemaHallService) injector
            .getInstance(CinemaHallService.class);
    private static MovieSessionService movieSessionService = (MovieSessionService) injector
            .getInstance(MovieSessionService.class);
    private static UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private static ShoppingCartService shoppingCartService = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private static OrderService orderService = (OrderService) injector
            .getInstance(OrderService.class);

    @Override
    protected void testAll() {
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
        bob.setEmail("bob1985@gmail.com");
        bob.setPassword("IAmBobAndYouAreNot");
        userService.add(bob);

        User alice = new User();
        alice.setEmail("AliceWhite17@gmail.com");
        alice.setPassword("Q9w8E7r6T5y4");
        userService.add(alice);

        shoppingCartService.registerNewShoppingCart(bob);
        shoppingCartService.addSession(tomorrowMovieSession, bob);

        shoppingCartService.registerNewShoppingCart(alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);

        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(bob);
        orderService.completeOrder(bobShoppingCart);
        System.out.println("\nBob's orders: ");
        orderService.getOrdersHistory(bob).forEach(System.out::println);

        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(alice);
        orderService.completeOrder(aliceShoppingCart);
        System.out.println("\nAlice's orders: ");
        orderService.getOrdersHistory(alice).forEach(System.out::println);
    }
}
