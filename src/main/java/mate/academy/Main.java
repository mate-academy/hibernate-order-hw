package mate.academy;

import java.time.LocalDateTime;
import java.util.List;
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
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_PASSWORD = "password";

    public static void main(String[] args) {
        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService =
                (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        try {
            AuthenticationService authenticationService = (AuthenticationService)
                    INJECTOR.getInstance(AuthenticationService.class);
            authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
            User user = authenticationService.login(TEST_EMAIL, TEST_PASSWORD);

            ShoppingCartService shoppingCartService = (ShoppingCartService)
                    INJECTOR.getInstance(ShoppingCartService.class);
            shoppingCartService.addSession(tomorrowMovieSession, user);
            shoppingCartService.addSession(yesterdayMovieSession, user);
            shoppingCartService.addSession(yesterdayMovieSession, user);

            ShoppingCart userShoppingCart = shoppingCartService.getByUser(user);

            OrderService orderService = (OrderService) INJECTOR.getInstance(OrderService.class);
            orderService.completeOrder(userShoppingCart);
            List<Order> userOrderList = orderService.getOrdersHistory(user);
            userOrderList.forEach(System.out::println);

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
