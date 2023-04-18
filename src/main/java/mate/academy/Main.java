package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    public static void main(String[] args) {
        // ---- Test MovieService ----
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);
        //  ---- Test CinemaHallService ----
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
        // ---- Test MovieSessionService ----
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
        // ---- Test AuthenticationService ----
        try {
            authenticationService.register("manager@cinema.net", "3d56hj81");
        } catch (RegistrationException e) {
            throw new RuntimeException("Can't register user ", e);
        }
        // ---- Test authorization ----
        User loginUser;
        try {
            loginUser = authenticationService.login("manager@cinema.net", "3d56hj81");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Can't login user ", e);
        }
        System.out.println(loginUser);
        // ---- Test shoppingCartService ----
        shoppingCartService.addSession(yesterdayMovieSession, loginUser);
        ShoppingCart shoppingCartPerson = shoppingCartService.getByUser(loginUser);
        System.out.println(shoppingCartPerson);
        System.out.println(shoppingCartPerson.getUser());
        System.out.println(shoppingCartPerson.getTickets());
        shoppingCartService.clear(shoppingCartPerson);
        System.out.println(shoppingCartPerson.getTickets());
        // ---- Test orderService ----
        Order order = orderService.completeOrder(shoppingCartPerson);
        System.out.println(order);
        System.out.println("---- Realization of ordering by shoppingCartPerson ---");
        System.out.println(shoppingCartPerson);
        List<Order> ordersHistoryByUser = orderService.getOrdersHistory(loginUser);
        System.out.println("---- Orders by person ---");
        ordersHistoryByUser.forEach(System.out::println);
    }
}
