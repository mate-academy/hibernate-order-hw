package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        Movie avatar = new Movie("Avatar");
        avatar.setDescription("Epic science fiction film directed by James Cameron");
        movieService.add(fastAndFurious);
        movieService.add(avatar);
        System.out.println(movieService.get(fastAndFurious.getId()));

        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession ffTomorrowMovieSession = new MovieSession();
        ffTomorrowMovieSession.setCinemaHall(firstCinemaHall);
        ffTomorrowMovieSession.setMovie(fastAndFurious);
        ffTomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession ffYesterdayMovieSession = new MovieSession();
        ffYesterdayMovieSession.setCinemaHall(firstCinemaHall);
        ffYesterdayMovieSession.setMovie(fastAndFurious);
        ffYesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSession avatarTomorrowMovieSession = new MovieSession();
        avatarTomorrowMovieSession.setCinemaHall(secondCinemaHall);
        avatarTomorrowMovieSession.setMovie(avatar);
        avatarTomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(ffTomorrowMovieSession);
        movieSessionService.add(ffYesterdayMovieSession);
        movieSessionService.add(avatarTomorrowMovieSession);

        System.out.println(movieSessionService.get(ffYesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User bob = new User();
        User alice = new User();
        try {
            bob = authenticationService.register("bob@mail.com", "1234");
            alice = authenticationService.register("alice@mail.com", "12345");
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(ffTomorrowMovieSession, bob);
        shoppingCartService.addSession(ffYesterdayMovieSession, alice);
        shoppingCartService.addSession(ffTomorrowMovieSession, alice);
        shoppingCartService.addSession(avatarTomorrowMovieSession, alice);

        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(bob);
        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(alice);

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(bobShoppingCart);
        orderService.completeOrder(aliceShoppingCart);

        System.out.println(orderService.getOrdersHistory(alice));
    }
}
