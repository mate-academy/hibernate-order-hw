package mate.academy;

import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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
import mate.academy.util.HashUtil;

public class Main {
    public static void main(String[] args) throws RegistrationException, AuthenticationException {

        Injector injector = Injector.getInstance("mate.academy");

        User user = new User();
        user.setEmail("andrii@ukr.net");
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword("zxcvb", user.getSalt()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        System.out.println(authenticationService.register("andrii@ukr.net", "zxcvb"));
        System.out.println(authenticationService.login("andrii@ukr.net", "zxcvb"));

        Movie movie = new Movie();
        movie.setTitle("Mad Max: Road Fury");
        movie.setDescription("Post-apocalyptic film");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        System.out.println(movieService.add(movie));

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(150);
        cinemaHall.setDescription("Blue hall");
        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        System.out.println(cinemaHallService.add(cinemaHall));

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movieService.get(3L));
        movieSession.setShowTime(LocalDateTime.now().plusDays(1L));
        movieSession.setCinemaHall(cinemaHallService.get(1L));
        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        System.out.println(movieSessionService.add(movieSession));

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User userFromDB = userService.findByEmail("andrii@ukr.net").get();
        MovieSession movieSessionFromDB = movieSessionService.get(3L);
        shoppingCartService.addSession(movieSessionFromDB, userFromDB);

        ShoppingCart shoppingCartFromDB
                = shoppingCartService.getByUser(userService.findByEmail("andrii@ukr.net").get());
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        System.out.println(orderService.completeOrder(shoppingCartFromDB));
        System.out.println(orderService
                .getOrdersHistory(userService.findByEmail("andrii@ukr.net").get()));
    }
}
