package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        final MovieService movieService = (MovieService)
                injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService = (MovieSessionService)
                injector.getInstance(MovieSessionService.class);
        final UserService userService = (UserService)
                injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        final OrderService orderService = (OrderService)
                injector.getInstance(OrderService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        System.out.println("Movie added: " + movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("First hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("Second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println("All Cinema Halls: " + cinemaHallService.getAll());

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setMovie(fastAndFurious);
        tomorrowSession.setCinemaHall(firstCinemaHall);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1));

        MovieSession yesterdaySession = new MovieSession();
        yesterdaySession.setMovie(fastAndFurious);
        yesterdaySession.setCinemaHall(firstCinemaHall);
        yesterdaySession.setShowTime(LocalDateTime.now().minusDays(1));

        movieSessionService.add(tomorrowSession);
        movieSessionService.add(yesterdaySession);

        System.out.println("Yesterday session: "
                + movieSessionService.get(yesterdaySession.getId()));
        System.out.println("Available sessions today: "
                + movieSessionService.findAvailableSessions(fastAndFurious.getId(),
                LocalDate.now()));

        User user = new User();
        user.setEmail("test1111111@example.com");
        user.setPassword("1111");
        userService.add(user);

        shoppingCartService.registerNewShoppingCart(user);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        shoppingCartService.addSession(tomorrowSession, user);
        Order order = orderService.completeOrder(shoppingCart);
        System.out.println("Completed order: " + order);

        System.out.println("Orders history:");
        orderService.getOrdersHistory(user).forEach(System.out::println);
    }
}
