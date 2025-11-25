package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        final User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("1234");
        userService.add(user);

        final Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription(
                "An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        System.out.println("Movie: " + movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        final CinemaHall hall1 = new CinemaHall();
        hall1.setCapacity(100);
        hall1.setDescription("first hall");

        final CinemaHall hall2 = new CinemaHall();
        hall2.setCapacity(200);
        hall2.setDescription("second hall");

        cinemaHallService.add(hall1);
        cinemaHallService.add(hall2);

        System.out.println(cinemaHallService.getAll());

        final MovieSession tomorrow = new MovieSession();
        tomorrow.setMovie(fastAndFurious);
        tomorrow.setCinemaHall(hall1);
        tomorrow.setShowTime(LocalDateTime.now().plusDays(1));

        final MovieSession yesterday = new MovieSession();
        yesterday.setMovie(fastAndFurious);
        yesterday.setCinemaHall(hall1);
        yesterday.setShowTime(LocalDateTime.now().minusDays(1));

        movieSessionService.add(tomorrow);
        movieSessionService.add(yesterday);

        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        shoppingCartService.registerNewShoppingCart(user);
        final ShoppingCart cart = shoppingCartService.getByUser(user);

        shoppingCartService.addSession(tomorrow, user);
        shoppingCartService.addSession(yesterday, user);

        System.out.println("Shopping cart tickets = " + cart.getTickets().size());

        final Order order = orderService.completeOrder(cart);
        System.out.println("ORDER CREATED ID = " + order.getId());

        final List<Order> history = orderService.getOrdersHistory(user);
        System.out.println("Order history: ");
        history.forEach(System.out::println);
    }
}
