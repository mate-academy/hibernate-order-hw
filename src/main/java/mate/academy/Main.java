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

        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);

        User bob = null;
        User alice = null;
        try {
            bob = authenticationService.register("bob@gmail.com", "1234");
            alice = authenticationService.register("alice@gmail.com", "5678");
            authenticationService.login("bob@gmail.com", "1234");
            authenticationService.login("alice@gmail.com", "5678");
        } catch (RegistrationException | AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        System.out.println("Bob's shopping cart after registration: "
                + shoppingCartService.getByUser(bob));
        System.out.println("Alice's shopping cart after registration: "
                + shoppingCartService.getByUser(alice));

        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        System.out.println("Bob's shopping cart after adding tomorrow session: "
                + shoppingCartService.getByUser(bob));
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        System.out.println("Alice's shopping cart after adding tomorrow session: "
                + shoppingCartService.getByUser(alice));

        OrderService orderService
                = (OrderService) injector.getInstance(OrderService.class);

        System.out.println("Bob's first order: "
                + orderService.completeOrder(shoppingCartService.getByUser(bob)));
        System.out.println("Bob's shopping cart after completed order: "
                + shoppingCartService.getByUser(bob));
        System.out.println("Alice's first order: "
                + orderService.completeOrder(shoppingCartService.getByUser(alice)));
        System.out.println("Alice's shopping cart after completed order: "
                + shoppingCartService.getByUser(alice));

        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        System.out.println("Bob's shopping cart after adding yesterday session: "
                + shoppingCartService.getByUser(bob));
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        System.out.println("Alice's shopping cart after adding yesterday session: "
                + shoppingCartService.getByUser(alice));

        System.out.println("Bob's second order: "
                + orderService.completeOrder(shoppingCartService.getByUser(bob)));
        System.out.println("Bob's shopping cart after completed order: "
                + shoppingCartService.getByUser(bob));
        System.out.println("Bob's second order: "
                + orderService.completeOrder(shoppingCartService.getByUser(alice)));
        System.out.println("Bob's shopping cart after completed order: "
                + shoppingCartService.getByUser(alice));

        System.out.println("Bob's all orders "
                + orderService.getOrdersHistory(bob));
        System.out.println("Alice's all orders "
                + orderService.getOrdersHistory(alice));

        orderService.getOrdersHistory(bob).forEach(System.out::println);
        orderService.getOrdersHistory(alice).forEach(System.out::println);
    }
}
