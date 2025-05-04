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

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

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

        //Check services and methods
        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);

        //Add new users.
        User alice = null;
        User bob = null;
        try {
            alice = authenticationService.register("Alice_MA@e-mail.com", "456");
            bob = authenticationService.register("Bob_MA@e-mail.com", "789");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, bob);

        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(alice);
        System.out.println(aliceShoppingCart);
        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(bob);
        System.out.println(bobShoppingCart);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(aliceShoppingCart);
        orderService.completeOrder(bobShoppingCart);

        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(tomorrowMovieSession, bob);

        ShoppingCart aliceSecondShoppingCart = shoppingCartService.getByUser(alice);
        System.out.println(aliceSecondShoppingCart);
        ShoppingCart bobSecondShoppingCart = shoppingCartService.getByUser(bob);
        System.out.println(bobSecondShoppingCart);

        orderService.completeOrder(aliceSecondShoppingCart);
        orderService.completeOrder(bobSecondShoppingCart);

        System.out.println(orderService.getOrdersHistory(alice));
        System.out.println(orderService.getOrdersHistory(bob));
    }
}
