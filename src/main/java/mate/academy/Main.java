package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
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
import mate.academy.util.HashUtil;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");

    @Inject
    private static final mate.academy.service.ShoppingCartService SHOPPINGCARTSERVICE
            = (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);

    private static final MovieService MOVIESERVICE
            = (MovieService) INJECTOR.getInstance(MovieService.class);

    @Inject
    private static final CinemaHallService CINEMAHALLSERVICE
            = (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);

    @Inject
    private static final MovieSessionService MOVIESESSIONSERVICE
            = (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
    @Inject
    private static final UserService USERSERVICE
            = (UserService) INJECTOR.getInstance(UserService.class);

    @Inject
    private static final AuthenticationService AUTHENTICATIONSERVICE
            = (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);

    @Inject
    private static final OrderService ORDERSERVICE
            = (OrderService) INJECTOR.getInstance(OrderService.class);

    public static void main(String[] args) throws AuthenticationException, RegistrationException {
        MovieService movieService = MOVIESERVICE;

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

        CinemaHallService cinemaHallService = CINEMAHALLSERVICE;
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

        MovieSessionService movieSessionService = MOVIESESSIONSERVICE;
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User bob = new User();
        bob.setSalt(HashUtil.getSalt());
        bob.setPassword("123456");
        bob.setEmail("bob@com.ua");

        AuthenticationService authenticationService = AUTHENTICATIONSERVICE;
        authenticationService.register(bob.getEmail(), bob.getPassword());
        User userBob = authenticationService.login("bob@com.ua", "123456");

        SHOPPINGCARTSERVICE.addSession(yesterdayMovieSession, userBob);
        SHOPPINGCARTSERVICE.addSession(tomorrowMovieSession, userBob);
        ShoppingCart shoppingCart = SHOPPINGCARTSERVICE.getByUser(userBob);

        OrderService orderService = ORDERSERVICE;
        orderService.completeOrder(shoppingCart);
        System.out.println(orderService.getOrdersHistory(userBob));

        SHOPPINGCARTSERVICE.clearShoppingCart(shoppingCart);
    }
}
