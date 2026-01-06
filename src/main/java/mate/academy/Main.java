package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static void main(String[] args) {
        MovieService movieService = null;

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

        CinemaHallService cinemaHallService = null;
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

        MovieSessionService movieSessionService = null;
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));
        // Test OrderService
        Injector injector = Injector.getInstance("mate.academy");

        User testUser = new User();
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("1234");

        UserService userService =
                (UserService) injector.getInstance(UserService.class);
        userService.add(testUser);

        Movie testMovie = new Movie();
        testMovie.setTitle("Matrix");

        MovieService movieService1 =
                (MovieService) injector.getInstance(MovieService.class);
        movieService1.add(testMovie);

        CinemaHall testHall = new CinemaHall();
        testHall.setCapacity(100);
        testHall.setDescription("Main hall");

        CinemaHallService cinemaHallService1 =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService1.add(testHall);

        MovieSession testSession = new MovieSession();
        testSession.setMovie(testMovie);
        testSession.setCinemaHall(testHall);
        testSession.setShowTime(LocalDateTime.now().plusDays(1));

        MovieSessionService movieSessionService1 =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService1.add(testSession);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(testSession, testUser);
        shoppingCartService.addSession(testSession, testUser);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        Order testOrder = orderService.completeOrder(
                shoppingCartService.getByUser(testUser)
        );

        System.out.println("Order created: " + testOrder);
        System.out.println("Tickets in order: " + testOrder.getTickets().size());
        System.out.println("Orders history size: "
                + orderService.getOrdersHistory(testUser).size());;
    }
}
