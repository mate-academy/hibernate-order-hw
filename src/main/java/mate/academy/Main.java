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
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        //CREATE NEW USER AND ADD TO DB
        User user = new User();
        user.setEmail("mateAlisson@gmail.com");
        user.setPassword("superbob");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        userService.add(user);
        //CREATE SHOPPING CART SERVICE
        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        //TEST METHOD REGISTER NEW SHOPPING CART
        shoppingCartService.registerNewShoppingCart(user);
        //TEST METHOD GET BY USER
        shoppingCartService.getByUser(user);
        //CREATE MOVIE AND ADD TO DB
        Movie breakingBad = new Movie();
        breakingBad.setTitle("Breaking Bad");
        breakingBad.setDescription("Awesome");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(breakingBad);
        //CREATE CINEMA HALL AND ADD TO DB
        CinemaHall bigCinemaHall = new CinemaHall();
        bigCinemaHall.setCapacity(100);
        bigCinemaHall.setDescription("New and Clean");
        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(bigCinemaHall);
        //CREATE MOVIE SESSION
        MovieSession movieSession = new MovieSession();
        movieSession.setShowTime(LocalDateTime.now());
        movieSession.setMovie(breakingBad);
        movieSession.setCinemaHall(bigCinemaHall);
        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);
        // TEST METHOD ADD SESSION
        shoppingCartService.addSession(movieSession, user);

        ShoppingCart byUserBob = shoppingCartService.getByUser(user);
        //TEST ORDER SERVICE
        OrderService orderService
                = (OrderService) injector.getInstance(OrderService.class);
        Order bobOrder = orderService.completeOrder(byUserBob);
        List<Order> ordersHistory = orderService.getOrdersHistory(user);
        System.out.println(ordersHistory);
    }
}
