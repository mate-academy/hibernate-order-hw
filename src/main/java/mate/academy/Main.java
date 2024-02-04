package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.RegistrationException;
import mate.academy.exception.UserNotFoundException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService = (CinemaHallService) INJECTOR
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService) INJECTOR
                .getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));
        AuthenticationService authenticationService = (AuthenticationService) INJECTOR
                .getInstance(AuthenticationService.class);
        User bob = null;
        try {
            bob = authenticationService.register("bob@gmail.com", "qwerty1234");
        } catch (RegistrationException e) {
            System.out.println("Cannot register user");
        }
        ShoppingCartService shoppingCartService = (ShoppingCartService) INJECTOR
                .getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(tomorrowMovieSession,bob);
        shoppingCartService.addSession(yesterdayMovieSession,bob);
        OrderService orderService = (OrderService) INJECTOR.getInstance(OrderService.class);
        Order order = orderService.completeOrder(shoppingCartService.getByUser(bob));
        System.out.println(order);
        try {
            System.out.println(orderService.getOrdersHistory(bob));
        } catch (UserNotFoundException e) {
            System.out.println("user is not already exist");
        }
        
        User unknownUser = null;
        try {
            unknownUser = authenticationService.register("asdasd@gmail.com", "qwerty1234");
        } catch (RegistrationException e) {
            System.out.println("Cannot register user");
        }

        shoppingCartService.addSession(tomorrowMovieSession,unknownUser);
        shoppingCartService.addSession(yesterdayMovieSession,unknownUser);

        try {
            System.out.println(orderService.getOrdersHistory(unknownUser));
        } catch (UserNotFoundException e) {
            //expected UserNotFoundExc
            System.out.println(e.getMessage());
        }
    }
}
