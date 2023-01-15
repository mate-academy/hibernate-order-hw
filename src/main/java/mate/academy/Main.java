package mate.academy;

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

public class Main {
    private static Injector injector = Injector.getInstance("mate");

    public static void main(String[] args) {
        final MovieService movieService = (MovieService)
                injector.getInstance(MovieService.class);
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        final CinemaHallService cinemaHallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
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

        final MovieSessionService movieSessionService = (MovieSessionService)
                injector.getInstance(MovieSessionService.class);
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

        final UserService userService = (UserService)
                injector.getInstance(UserService.class);
        User bob = new User();
        bob.setEmail("bob@gm.com");
        bob.setPassword("123456");
        User alice = new User();
        alice.setEmail("alice@gm.com");
        alice.setPassword("qwerty");
        userService.add(bob);
        userService.add(alice);

        final ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(bob);
        shoppingCartService.registerNewShoppingCart(alice);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, alice);

        final OrderService orderService = (OrderService)
                injector.getInstance(OrderService.class);
        ShoppingCart bobCart = shoppingCartService.getByUser(bob);
        orderService.completeOrder(bobCart);
        shoppingCartService.clearShoppingCart(bobCart);
        ShoppingCart aliceCart = shoppingCartService.getByUser(alice);
        orderService.completeOrder(aliceCart);
        shoppingCartService.clearShoppingCart(aliceCart);
    }
}
