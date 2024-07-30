package mate.academy;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        final AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);

        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);

        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);

        final UserService userService =
                (UserService) injector.getInstance(UserService.class);

        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        final OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);

        try {
            String email = "john.doe@example.com";
            String password = "password123";
            User registeredUser = authenticationService.register(email, password);
            System.out.println("User registered: " + registeredUser);

            User loggedInUser = authenticationService.login(email, password);
            System.out.println("User logged in: " + loggedInUser);

            CinemaHall cinemaHall1 = new CinemaHall();
            cinemaHall1.setCapacity(100);
            cinemaHall1.setDescription("Large hall for blockbuster movies");
            cinemaHallService.add(cinemaHall1);

            CinemaHall cinemaHall2 = new CinemaHall();
            cinemaHall2.setCapacity(50);
            cinemaHall2.setDescription("Small hall for indie movies");
            cinemaHallService.add(cinemaHall2);

            Movie movie1 = new Movie();
            movie1.setTitle("Avengers: Endgame");
            movie1.setDescription("Superhero movie with epic battles");
            movieService.add(movie1);

            Movie movie2 = new Movie();
            movie2.setTitle("Parasite");
            movie2.setDescription("Award-winning Korean thriller");
            movieService.add(movie2);

            MovieSession session1 = new MovieSession();
            session1.setMovie(movie1);
            session1.setCinemaHall(cinemaHall1);
            session1.setShowTime(LocalDateTime.now().plusDays(1));
            movieSessionService.add(session1);

            MovieSession session2 = new MovieSession();
            session2.setMovie(movie2);
            session2.setCinemaHall(cinemaHall2);
            session2.setShowTime(LocalDateTime.now().plusDays(2));
            movieSessionService.add(session2);

            shoppingCartService.addSession(session1, loggedInUser);
            shoppingCartService.addSession(session2, loggedInUser);

            ShoppingCart shoppingCart = shoppingCartService.getByUser(loggedInUser);
            Order order = orderService.completeOrder(shoppingCart);
            System.out.println("\nOrder created:");
            System.out.println(order);

            List<CinemaHall> cinemaHalls = cinemaHallService.getAll();
            System.out.println("\nCinema Halls:");
            cinemaHalls.forEach(System.out::println);

            List<Movie> movies = movieService.getAll();
            System.out.println("\nMovies:");
            movies.forEach(System.out::println);

            List<MovieSession> movieSessions = movieSessionService
                    .findAvailableSessions(movie1.getId(),
                            LocalDateTime.now().plusDays(1).toLocalDate());
            System.out.println("\nMovie Sessions:");
            movieSessions.forEach(System.out::println);

            System.out.println("\nUser:");
            System.out.println(loggedInUser);

            shoppingCart = shoppingCartService.getByUser(loggedInUser);
            System.out.println("\nShopping Cart:");
            System.out.println(shoppingCart);

            List<Order> ordersHistory = orderService.getOrdersHistory(loggedInUser);
            System.out.println("\nOrders History:");
            ordersHistory.forEach(System.out::println);

        } catch (AuthenticationException | RegistrationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
