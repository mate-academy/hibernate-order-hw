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
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");
    private static final String EMAIL = "bob@email.com";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) throws RegistrationException {
        MovieService movieService =
                (MovieService) INJECTOR.getInstance(MovieService.class);

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
                (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession nextMovieSession = new MovieSession();
        nextMovieSession.setCinemaHall(firstCinemaHall);
        nextMovieSession.setMovie(fastAndFurious);
        nextMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(nextMovieSession);

        System.out.println(movieSessionService.get(nextMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        UserService userService =
                (UserService) INJECTOR.getInstance(UserService.class);

        AuthenticationService authenticationService =
                (AuthenticationService) INJECTOR.getInstance(
                        AuthenticationService.class);
        try {
            authenticationService.register(EMAIL, PASSWORD);
        } catch (RegistrationException registrationException) {
            System.err.println("Registration failed. This email: " + EMAIL + " already exists.");
        }

        User bob = userService.findByEmail(EMAIL).get();
        ShoppingCartService cartService =
                (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);

        cartService.addSession(tomorrowMovieSession, bob);
        cartService.addSession(nextMovieSession, bob);
        ShoppingCart cart = cartService.getByUser(bob);

        OrderService orderService =
                (OrderService) INJECTOR.getInstance(OrderService.class);
        orderService.completeOrder(cart);
        orderService.getOrdersHistory(bob);
    }
}
