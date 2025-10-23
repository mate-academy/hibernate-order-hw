package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
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
        CinemaHall cinemaHallInstance = new CinemaHall();
        cinemaHallInstance.setCapacity(100);
        cinemaHallInstance.setDescription("Cinema Hall");

        CinemaHall cinemaHallInstance2 = new CinemaHall();
        cinemaHallInstance2.setCapacity(200);
        cinemaHallInstance2.setDescription("Cinema Hall2");

        CinemaHallService cinemaHallServiceInstance =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallServiceInstance.add(cinemaHallInstance);
        cinemaHallServiceInstance.add(cinemaHallInstance2);

        System.out.println(cinemaHallServiceInstance.get(cinemaHallInstance.getId()));
        System.out.println(cinemaHallServiceInstance.getAll());

        Movie movieInstance = new Movie();
        movieInstance.setTitle("Movie Title 1");
        movieInstance.setDescription("Movie Description 1");

        Movie movieInstance2 = new Movie();
        movieInstance2.setTitle("Movie Title 2");
        movieInstance2.setDescription("Movie Description 2");

        MovieService movieServiceInstance =
                (MovieService) injector.getInstance(MovieService.class);
        movieServiceInstance.add(movieInstance);
        movieServiceInstance.add(movieInstance2);

        System.out.println(movieServiceInstance.get(movieInstance.getId()));
        System.out.println(movieServiceInstance.getAll());

        MovieSession movieSessionInstance = new MovieSession();
        movieSessionInstance.setMovie(movieInstance);
        movieSessionInstance.setCinemaHall(cinemaHallInstance);
        movieSessionInstance.setShowTime(
                LocalDateTime.of(2020, 10, 20, 18, 10));

        MovieSession movieSessionInstance2 = new MovieSession();
        movieSessionInstance2.setMovie(movieInstance2);
        movieSessionInstance2.setCinemaHall(cinemaHallInstance2);
        movieSessionInstance2.setShowTime(
                LocalDateTime.of(2020, 9, 15, 15, 30));

        MovieSessionService movieSessionServiceInstance =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionServiceInstance.add(movieSessionInstance);
        movieSessionServiceInstance.add(movieSessionInstance2);

        System.out.println(movieSessionServiceInstance.get(movieSessionInstance.getId()));
        System.out.println(movieSessionServiceInstance.findAvailableSessions(
                movieInstance.getId(), movieSessionInstance2.getShowTime()
                        .toLocalDate()));

        User user = new User();
        user.setEmail("example_email@example.com");
        user.setPassword("example_password");

        User user2 = new User();
        user2.setEmail("exaple_email2@example.com");
        user2.setPassword("example_password2");

        UserService userService = (UserService)
                injector.getInstance(UserService.class);
        userService.add(user);
        userService.add(user2);

        ShoppingCartService shoppingCartServiceInstance =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartServiceInstance.registerNewShoppingCart(user);
        shoppingCartServiceInstance.registerNewShoppingCart(user2);

        shoppingCartServiceInstance.addSession(movieSessionInstance, user);
        shoppingCartServiceInstance.addSession(movieSessionInstance2, user2);

        System.out.println(shoppingCartServiceInstance.getByUser(user));
        System.out.println(shoppingCartServiceInstance.getByUser(user2));

        shoppingCartServiceInstance.clearShoppingCart(shoppingCartServiceInstance.getByUser(user));

        System.out.println(shoppingCartServiceInstance.getByUser(user));

        OrderService orderServiceInstance = (OrderService) injector.getInstance(OrderService.class);

        orderServiceInstance.completeOrder(shoppingCartServiceInstance.getByUser(user));
        orderServiceInstance.completeOrder(shoppingCartServiceInstance.getByUser(user2));

        System.out.println(orderServiceInstance.getOrdersHistory(user));
        System.out.println(orderServiceInstance.getOrdersHistory(user2));

    }
}
