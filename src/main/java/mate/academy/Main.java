package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
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

        // Test methods from UserService, MovieService, and ShoppingCartService if needed
        Injector injector = Injector.getInstance("mate.academy");

        UserService userService = (UserService) injector.getInstance(UserService.class);
        User user = new User();
        user.setEmail("JohnDoe@gmail.com");
        user.setPassword("password");
        userService.add(user);

        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDescription("Sci-Fi");

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));
        movieService.add(movie);
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        shoppingCartService.addSession(movieSession, user);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        Order order = orderService.completeOrder(shoppingCart);
        System.out.println("Order completed: " + order);

        List<Order> orderHistory = orderService.getOrdersHistory(user);
        System.out.println("Order history for user " + user.getEmail() + ": " + orderHistory);
    }
}
