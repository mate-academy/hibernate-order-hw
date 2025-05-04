package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(fastAndFurious);

        CinemaHall blueHall = new CinemaHall();
        blueHall.setCapacity(60);
        blueHall.setDescription("Cute blue hall");

        CinemaHall yellowHall = new CinemaHall();
        yellowHall.setCapacity(60);
        yellowHall.setDescription("Cute yellow hall");
        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(blueHall);
        cinemaHallService.add(yellowHall);

        MovieSession todaySession1 = new MovieSession();
        todaySession1.setMovie(fastAndFurious);
        todaySession1.setCinemaHall(blueHall);
        todaySession1.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 15)));

        MovieSession todaySession2 = new MovieSession();
        todaySession2.setMovie(fastAndFurious);
        todaySession2.setCinemaHall(yellowHall);
        todaySession2.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 15)));
        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(todaySession1);
        movieSessionService.add(todaySession2);

        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("qwerty");

        User alice = new User();
        alice.setEmail("alice@gmail.com");
        alice.setPassword("qwerty");
        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        authenticationService.register(bob.getEmail(), bob.getPassword());
        authenticationService.register(alice.getEmail(), alice.getPassword());
        User loginedBob = authenticationService.login(bob.getEmail(), bob.getPassword());
        User loginedAlice = authenticationService.login(alice.getEmail(), alice.getPassword());

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(todaySession1, loginedBob);
        shoppingCartService.addSession(todaySession1, loginedBob);
        shoppingCartService.addSession(todaySession1, loginedAlice);
        shoppingCartService.addSession(todaySession2, loginedAlice);
        shoppingCartService.addSession(todaySession2, loginedAlice);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(shoppingCartService.getByUser(loginedBob));
        orderService.completeOrder(shoppingCartService.getByUser(loginedAlice));

        shoppingCartService.addSession(todaySession1, loginedBob);

        orderService.completeOrder(shoppingCartService.getByUser(loginedBob));
        orderService.getOrdersHistory(loginedBob).forEach(System.out::println);
        orderService.getOrdersHistory(loginedAlice).forEach(System.out::println);
    }
}
