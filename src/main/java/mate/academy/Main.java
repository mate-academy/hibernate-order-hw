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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        final String emailOfBob = "Bob@gmail.com";
        final String passwordOfBob = "Bob1234";
        final String emailOfAlice = "Alice@gmail.com";
        final String passwordOfAlice = "Alice1234";

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
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        System.out.println("Register user Bob!");
        try {
            authenticationService.register(emailOfBob, passwordOfBob);
        } catch (RegistrationException e) {
            throw new RuntimeException("Can not register this user", e);
        }
        System.out.println("Register user Alice!");
        try {
            authenticationService.register(emailOfAlice, passwordOfAlice);
        } catch (RegistrationException e) {
            throw new RuntimeException("Can not register this user", e);
        }

        UserService userService = (UserService) injector
                .getInstance(UserService.class);
        System.out.println("Get user Bob by email!");
        User userBob = userService.findByEmail(emailOfBob).orElseThrow();
        System.out.println(userBob);
        System.out.println("Get user Alice by email!");
        User userAlice = userService.findByEmail(emailOfAlice).orElseThrow();
        System.out.println(userAlice);

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        System.out.println("Get shopping cart for Alice!");
        System.out.println(shoppingCartService.getByUser(userAlice));
        System.out.println("Add tomorrowMovieSession to Alice's shopping cart!");
        shoppingCartService.addSession(tomorrowMovieSession, userAlice);
        ShoppingCart shoppingCartOfAlice = shoppingCartService.getByUser(userAlice);
        System.out.println(shoppingCartOfAlice);

        OrderService orderService = (OrderService) injector
                .getInstance(OrderService.class);
        System.out.println("Complete Alice's order!");
        orderService.completeOrder(shoppingCartOfAlice);
        System.out.println("Check Alice's tickets in shopping cart!");
        System.out.println(shoppingCartService.getByUser(userAlice));

        System.out.println("Get Alice's order history!");
        System.out.println(orderService.getOrdersHistory(userAlice));
        System.out.println("Get Bob's order history!");
        System.out.println(orderService.getOrdersHistory(userBob));
    }
}
