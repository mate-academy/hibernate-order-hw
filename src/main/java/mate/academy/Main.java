package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService)
                injector.getInstance(MovieService.class);

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

        CinemaHallService cinemaHallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(firstCinemaHall);
        todayMovieSession.setMovie(fastAndFurious);
        todayMovieSession.setShowTime(LocalDateTime.now());

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService)
                injector.getInstance(MovieSessionService.class);
        movieSessionService.add(todayMovieSession);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = (AuthenticationService)
                injector.getInstance(AuthenticationService.class);
        User bob;
        User alice;
        try {
            authenticationService.register("bob@gmail.com", "qwerty");
            authenticationService.register("alice@gmail.com", "12345");
            bob = authenticationService.login("bob@gmail.com", "qwerty");
            alice = authenticationService.login("alice@gmail.com", "12345");
        } catch (RegistrationException e) {
            throw new RuntimeException("Can't register user", e);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Can't login user", e);
        }

        ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(todayMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, bob);

        System.out.println(shoppingCartService.getByUser(alice));
        System.out.println(shoppingCartService.getByUser(bob));

        shoppingCartService.clearShoppingCart(shoppingCartService.getByUser(alice));
        System.out.println(shoppingCartService.getByUser(alice));

        OrderService orderService = (OrderService)
                injector.getInstance(OrderService.class);
        System.out.println(orderService.completeOrder(shoppingCartService.getByUser(bob)));
        orderService.getOrdersHistory(bob).forEach(System.out::println);
    }
}
