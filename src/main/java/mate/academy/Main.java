package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
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
        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User firstUser = null;
        User secondUser = null;
        User thirdUser = null;
        try {
            firstUser = authenticationService.register("first_user@gmail.com", "zxcvbn");
            secondUser = authenticationService.register("second_user@gmail.com", "123456");
            thirdUser = authenticationService.register("third_user@gmail.com", "098765");
            System.out.println(authenticationService.login("first_user@gmail.com", "zxcvbn"));
            System.out.println(authenticationService.login("second_user@gmail.com", "123456"));
            System.out.println(authenticationService.login("third_user@gmail.com", "123456"));
            System.out.println(authenticationService.login("fourth_user@gmail.com", "098765"));
        } catch (RegistrationException | AuthenticationException e) {
            System.out.println(e);
        }
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        Movie taxiPart1 = new Movie("Taxi-1");
        taxiPart1.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(taxiPart1);

        Movie harryPotterPart1 = new Movie("Harry Potter and Philosopher Stone");
        harryPotterPart1
                .setDescription("An fantasy film about magic, school and fantastic animals");
        movieService.add(harryPotterPart1);

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

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(taxiPart1);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(firstCinemaHall);
        todayMovieSession.setMovie(harryPotterPart1);
        todayMovieSession.setShowTime(LocalDateTime.now());

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);
        movieSessionService.add(todayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(tomorrowMovieSession, secondUser);
        shoppingCartService.addSession(yesterdayMovieSession, secondUser);
        shoppingCartService.addSession(yesterdayMovieSession, firstUser);
        shoppingCartService.addSession(todayMovieSession, firstUser);
        shoppingCartService.addSession(todayMovieSession, thirdUser);

        try {
            System.out.println(shoppingCartService
                    .getByUser(authenticationService.login("first_user@gmail.com", "zxcvbn")));
            System.out.println(shoppingCartService
                    .getByUser(authenticationService.login("second_user@gmail.com", "123456")));
        } catch (AuthenticationException e) {
            System.out.println(e);;
        }

        OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);

        orderService.completeOrder(shoppingCartService.getByUser(firstUser));
        orderService.completeOrder(shoppingCartService.getByUser(secondUser));

        shoppingCartService.addSession(yesterdayMovieSession, firstUser);
        shoppingCartService.addSession(todayMovieSession, firstUser);

        orderService.completeOrder(shoppingCartService.getByUser(firstUser));

        orderService.getOrdersHistory(firstUser).forEach(System.out::println);
        orderService.getOrdersHistory(secondUser).forEach(System.out::println);
    }
}
