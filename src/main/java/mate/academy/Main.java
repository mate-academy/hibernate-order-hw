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

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        AuthenticationService authService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing.");
        movieService.add(fastAndFurious);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("IMAX");
        cinemaHallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(fastAndFurious);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(movieSession);

        User user = null;
        try {
            user = authService.register("szymon@example.com", "1234");
        } catch (RegistrationException e) {
            try {
                user = authService.login("szymon@example.com", "1234");
            } catch (AuthenticationException loginException) {
                throw new RuntimeException("Can't authenticate user", loginException);
            }
        }

        shoppingCartService.addSession(movieSession, user);

        System.out.println("--- Processing Order ---");
        ShoppingCart cart = shoppingCartService.getByUser(user);

        Order order = orderService.completeOrder(cart);
        System.out.println("Completed Order ID: " + order.getId());

        List<Order> history = orderService.getOrdersHistory(user);
        System.out.println("Order History for " + user.getEmail() + ":");
        for (Order o : history) {
            System.out.println("Order ID: " + o.getId() + ", Date: " + o.getOrderDate());
        }

        ShoppingCart cartAfter = shoppingCartService.getByUser(user);
        System.out.println("Cart tickets count after order: " + cartAfter.getTickets().size());
    }
}