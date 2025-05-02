package mate.academy;

import java.time.LocalDateTime;
import java.util.List;
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

public class Main {
    private static final Injector injector = Injector
            .getInstance("mate.academy");

    public static void main(String[] args) {
        final AuthenticationService authService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        User user;
        try {
            user = authService.register("anna@example.com",
                    "passwd");
            System.out.println("Registered new user: "
                    + user.getEmail());
        } catch (Exception e) {
            System.out.println("Registration failed (" + e.getMessage()
                    + "), attempting to log in...");
            try {
                user = authService.login("anna@example.com",
                        "passwd");
                System.out.println("Logged in existing user: "
                        + user.getEmail());
            } catch (Exception loginEx) {
                System.err.println("Login failed: "
                        + loginEx.getMessage());
                return;
            }
        }

        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        final Movie movie = new Movie("Inception");
        movie.setDescription("Sci-fi about dreams within dreams");
        movieService.add(movie);

        final CinemaHallService hallService =
                (CinemaHallService) injector
                        .getInstance(CinemaHallService.class);
        final CinemaHall hall = new CinemaHall();
        hall.setCapacity(50);
        hall.setDescription("Hall 1");
        hallService.add(hall);

        final MovieSessionService sessionService =
                (MovieSessionService) injector
                        .getInstance(MovieSessionService.class);
        final MovieSession session = new MovieSession();
        session.setMovie(movie);
        session.setCinemaHall(hall);
        session.setShowTime(LocalDateTime.now().plusDays(1));
        sessionService.add(session);

        final ShoppingCartService cartService =
                (ShoppingCartService) injector
                        .getInstance(ShoppingCartService.class);
        cartService.addSession(session, user);
        cartService.addSession(session, user);

        final ShoppingCart cart = cartService.getByUser(user);
        final OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);
        final Order order = orderService.completeOrder(cart);
        System.out.println("Placed order: " + order);

        final List<Order> history = orderService
                .getOrdersHistory(user);
        System.out.println("Order history for "
                + user.getEmail() + ":");
        history.forEach(System.out::println);
    }
}
