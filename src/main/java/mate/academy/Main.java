package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);

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

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
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

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(secondCinemaHall);
        todayMovieSession.setMovie(fastAndFurious);
        todayMovieSession.setShowTime(LocalDateTime.now());

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);
        movieSessionService.add(todayMovieSession);
        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService auth =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        try {
            auth.register("globaroman@gmail.com", "qwerty");
            auth.register("test@gmail.com", "test123");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
        User user1 = null;
        User user2 = null;
        try {
            user1 = auth.login("globaroman@gmail.com", "qwerty");
            user2 = auth.login("test@gmail.com", "test123");
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        ShoppingCartService shopCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shopCartService.addSession(tomorrowMovieSession, user1);
        shopCartService.addSession(tomorrowMovieSession, user2);
        shopCartService.addSession(yesterdayMovieSession, user1);

        ShoppingCart cartFromDB1 = shopCartService.getByUser(user1);
        System.out.println();
        System.out.println(cartFromDB1);
        ShoppingCart cartFromDB2 = shopCartService.getByUser(user2);
        System.out.println(cartFromDB2);
        System.out.println();

        OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(cartFromDB1);
        orderService.completeOrder(cartFromDB2);

        shopCartService.addSession(todayMovieSession, user1);
        ShoppingCart cartFromDB3 = shopCartService.getByUser(user1);
        orderService.completeOrder(cartFromDB3);

        orderService.getOrdersHistory(user1).forEach(System.out::println);
        orderService.getOrdersHistory(user2).forEach(System.out::println);
    }
}
