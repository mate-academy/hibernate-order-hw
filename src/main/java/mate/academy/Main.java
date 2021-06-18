package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService = (MovieService)
            injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService)
            injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService)
            injector.getInstance(MovieSessionService.class);
    private static final UserService userService = (UserService)
            injector.getInstance(UserService.class);
    private static final AuthenticationService authenticationService = (AuthenticationService)
            injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService = (OrderService)
            injector.getInstance(OrderService.class);

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

        System.out.println("--------user service--------");
        System.out.println(authenticationService.register("alice@gmail.com", "1234"));
        System.out.println(authenticationService.register("bob@gmail.com", "bob1234"));
        User bob = userService.findByEmail("bob@gmail.com").get();
        User alice = userService.findByEmail("alice@gmail.com").get();
        System.out.println(bob);
        System.out.println(alice);

        System.out.println("------shopping cart service------");
        shoppingCartService.registerNewShoppingCart(bob);
        shoppingCartService.registerNewShoppingCart(alice);
        ShoppingCart bobsShopCard = shoppingCartService.getByUser(bob);
        ShoppingCart alicesShopCard = shoppingCartService.getByUser(alice);
        System.out.println(bobsShopCard);
        System.out.println(alicesShopCard);
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        bobsShopCard = shoppingCartService.getByUser(bob);
        alicesShopCard = shoppingCartService.getByUser(alice);
        System.out.println(bobsShopCard);
        System.out.println(alicesShopCard);

        System.out.println("------order service------");
        Order bobsOrder = orderService.completeOrder(bobsShopCard);
        System.out.println(bobsOrder);
        System.out.println(bobsShopCard);
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        Order bobsNewOrder = orderService.completeOrder(shoppingCartService.getByUser(bob));
        System.out.println(bobsNewOrder);
        orderService.getOrdersHistory(bob).forEach(System.out::println);
    }
}
