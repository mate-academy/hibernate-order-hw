package mate.academy;

import java.time.LocalDateTime;
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
    private static final MovieService movieService = (MovieService)
            injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService)
            injector.getInstance(CinemaHallService.class);
    private static final AuthenticationService authenticationService = (AuthenticationService)
            injector.getInstance(AuthenticationService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService)
            injector.getInstance(MovieSessionService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    public static void main(String[] args) {
        Movie firstMovie = new Movie();
        firstMovie.setTitle("First movie");
        firstMovie.setDescription("First movie description");

        Movie secondMovie = new Movie();
        secondMovie.setTitle("Second movie");
        secondMovie.setDescription("Second movie description");

        movieService.add(firstMovie);
        movieService.add(secondMovie);

        CinemaHall firstHall = new CinemaHall();
        firstHall.setCapacity(10);
        firstHall.setDescription("First hall");

        CinemaHall secondHall = new CinemaHall();
        secondHall.setCapacity(10);
        secondHall.setDescription("Second hall");

        cinemaHallService.add(firstHall);
        cinemaHallService.add(secondHall);

        MovieSession yesterdayFirstSession = new MovieSession();
        yesterdayFirstSession.setMovie(firstMovie);
        yesterdayFirstSession.setCinemaHall(firstHall);
        yesterdayFirstSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSession yesterdaySecondSession = new MovieSession();
        yesterdaySecondSession.setMovie(secondMovie);
        yesterdaySecondSession.setCinemaHall(secondHall);
        yesterdaySecondSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setCinemaHall(secondHall);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1L));
        tomorrowSession.setMovie(secondMovie);

        movieSessionService.add(yesterdayFirstSession);
        movieSessionService.add(yesterdaySecondSession);
        movieSessionService.add(tomorrowSession);

        User bob;
        User alice;
        ShoppingCart aliceCart;
        ShoppingCart bobCart;
        try {
            bob = authenticationService.register("bob@mail.com", "bob");
            alice = authenticationService.register("alice@mail.com", "alice");
            aliceCart = shoppingCartService.getByUser(alice);
            bobCart = shoppingCartService.getByUser(bob);
        } catch (RegistrationException e) {
            throw new RuntimeException("Can't register user");
        }

        shoppingCartService.addSession(yesterdayFirstSession, alice);
        shoppingCartService.addSession(yesterdaySecondSession, alice);

        shoppingCartService.addSession(yesterdayFirstSession, bob);
        shoppingCartService.addSession(tomorrowSession, bob);

        System.out.println("Bobs tickets first order");
        shoppingCartService.getByUser(bob).getTickets().forEach(System.out::println);
        Order bobOrder = orderService.completeOrder(bobCart);
        System.out.println("Bobs order " + bobOrder);
        orderService.completeOrder(aliceCart);

        shoppingCartService.addSession(tomorrowSession, alice);

        orderService.completeOrder(shoppingCartService.getByUser(alice));
    }
}
