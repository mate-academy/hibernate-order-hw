package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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
        Movie fastAndFurious = createMovie("Fast and Furious",
                "An action film about street racing, heists, and spies");
        Movie needForSpeed2 = createMovie("Need For Speed",
                "The series generally centers around illicit" +
                        " street racing and tasks players to complete various" +
                        " types of races while evading the local law enforcement in police pursuits.");
        Movie gameOfThrones = createMovie("Game of Thrones",
                "Game of Thrones is an American fantasy drama" +
                        " television series created by David Benioff and D. B. Weiss for HBO.");
        Movie snowden = createMovie("Snowden", "About former CIA operative Edward Snowden, " +
                "who made one of the biggest revelations");
        System.out.println("Move with id = 2: " + movieService.get(2L));
        System.out.println("All movies in Db: ");
        movieService.getAll().forEach(System.out::println);
        System.out.println(System.lineSeparator());

        CinemaHall cinemaHall1 = createCinemaHall(50, "Hall #1");
        CinemaHall cinemaHall2 = createCinemaHall(100, "Hall #2");
        CinemaHall cinemaHall3 = createCinemaHall(200, "Hall #3");
        CinemaHall cinemaHall4 = createCinemaHall(250, "Hall #4");
        System.out.println("Cinema hall with id = 1: " + cinemaHallService.get(1L));
        System.out.println("All cinema hall in Db: ");
        cinemaHallService.getAll().forEach(System.out::println);
        System.out.println(System.lineSeparator());

        MovieSession movieSession1 = createMovieSession(fastAndFurious, cinemaHall1,
                LocalDateTime.of(2020, 2, 12, 11,30));
        MovieSession movieSession2 = createMovieSession(fastAndFurious, cinemaHall2,
                LocalDateTime.of(2022, 1, 21, 19,30));
        MovieSession movieSession3 = createMovieSession(fastAndFurious, cinemaHall3,
                LocalDateTime.of(2022, 1, 3, 19,30));
        MovieSession movieSession4= createMovieSession(needForSpeed2, cinemaHall1,
                LocalDateTime.of(2022, 1, 25, 15,30));
        System.out.println("MovieSession with id = 4: " + movieSessionService.get(4L));
        System.out.println("Available movie sessions 21.01.2022 with movie: " + fastAndFurious);
        movieSessionService.findAvailableSessions(fastAndFurious.getId(),
                LocalDate.of(2022, 1, 25)).forEach(System.out::println);
        System.out.println(System.lineSeparator());

        authenticationService.register("bobovich@gmail.com", "123456789");
        authenticationService.register("alice@gmail.com", "123456789");
        authenticationService.register("john@gmail.com", "3325553");

        User bob = null;
        User alice = null;
        User john = null;
        try {
            bob = authenticationService.login("bobovich@gmail.com", "123456789");
            alice = authenticationService.login("alice@gmail.com", "123456789");
            john = authenticationService.login("john@gmail.com", "3325553");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        shoppingCartService.addSession(movieSession1, bob);
        shoppingCartService.addSession(movieSession2, bob);
        shoppingCartService.addSession(movieSession3, bob);
        shoppingCartService.addSession(movieSession4, bob);
        shoppingCartService.addSession(movieSession1, alice);
        shoppingCartService.addSession(movieSession2, alice);
        shoppingCartService.addSession(movieSession4, john);
        ShoppingCart shoppingCartBob = shoppingCartService.getByUser(bob);
        ShoppingCart shoppingCartAlice = shoppingCartService.getByUser(alice);
        ShoppingCart shoppingCartJohn = shoppingCartService.getByUser(john);
        System.out.println(bob + " shopping cart: " + shoppingCartBob);
        System.out.println(alice + " shopping cart: " + shoppingCartAlice);
        System.out.println(john + " shopping cart: " + shoppingCartJohn);
        orderService.completeOrder(shoppingCartBob);
        orderService.completeOrder(shoppingCartAlice);
        orderService.completeOrder(shoppingCartJohn);
        shoppingCartService.addSession(movieSession1, bob);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(bob);
        orderService.completeOrder(shoppingCart);
        System.out.println("John history: ");
        orderService.getOrdersHistory(john).forEach(System.out::println);
        System.out.println("Bob history: ");
        orderService.getOrdersHistory(bob).forEach(System.out::println);
        System.out.println("Alice history: ");
        orderService.getOrdersHistory(alice).forEach(System.out::println);
    }

    private static Movie createMovie(String name, String description) {
        Movie movie = new Movie(name);
        movie.setDescription(description);
        movieService.add(movie);
        return movie;
    }

    private static CinemaHall createCinemaHall(int capacity, String description) {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(capacity);
        cinemaHall.setDescription(description);
        cinemaHallService.add(cinemaHall);
        return cinemaHall;
    }

    private static MovieSession createMovieSession (Movie movie ,
             CinemaHall cinemaHall, LocalDateTime dateTime) {
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(dateTime);
        movieSessionService.add(movieSession);
        return movieSession;
    }
}
