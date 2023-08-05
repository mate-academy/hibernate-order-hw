package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService = (MovieService)
            injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService)
            injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService)
            injector.getInstance(MovieSessionService.class);
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

        // Register ///////////////////////////////////////////////////////////////////////////////
        User userSarahConor;
        String loginSarahConor = "userSarahConor@gmail.com";
        String passwordSarahConor = "NoFate";
        User userJohnConor;
        String loginJohnConor = "userJohnConor@gmail.com";
        String passwordJohnConor = "AstaLaVista";
        try {
            userSarahConor = authenticationService.register(loginSarahConor, passwordSarahConor);
            userJohnConor = authenticationService.register(loginJohnConor, passwordJohnConor);
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
        //try {
        //    System.out.println(authenticationService.login(loginSarahConor, passwordSarahConor));
        //    System.out.println(authenticationService.login(loginJohnConor, passwordJohnConor));
        //} catch (AuthenticationException e) {
        //    throw new RuntimeException(e);
        //}

        // ShoppingCart ///////////////////////////////////////////////////////////////////////////
        shoppingCartService.addSession(tomorrowMovieSession, userSarahConor);
        shoppingCartService.addSession(tomorrowMovieSession, userJohnConor);
        shoppingCartService.addSession(yesterdayMovieSession, userJohnConor);

        ShoppingCart shoppingCartBySarahConor = shoppingCartService.getByUser(userSarahConor);
        ShoppingCart shoppingCartByJohnConor = shoppingCartService.getByUser(userJohnConor);

        shoppingCartService.clearShoppingCart(shoppingCartBySarahConor);
        shoppingCartService.clearShoppingCart(shoppingCartByJohnConor);

        System.out.println(shoppingCartService.getByUser(userSarahConor));
        System.out.println(shoppingCartService.getByUser(userJohnConor));

        // Order //////////////////////////////////////////////////////////////////////////////////
        orderService.completeOrder(shoppingCartService.getByUser(userSarahConor));
        orderService.completeOrder(shoppingCartService.getByUser(userJohnConor));

        orderService.getOrdersHistory(userSarahConor).forEach(System.out::println);
        orderService.getOrdersHistory(userJohnConor).forEach(System.out::println);
    }
}
