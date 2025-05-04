package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        final MovieService movieService
                = (MovieService) injector.getInstance(MovieService.class);
        final MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final OrderService orderService
                = (OrderService) injector.getInstance(OrderService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        Movie interstellar = new Movie("Interstellar");
        interstellar.setDescription("science-fiction");
        movieService.add(interstellar);

        CinemaHall cinemaHallNorth = new CinemaHall();
        cinemaHallNorth.setCapacity(250);
        cinemaHallNorth.setDescription("North wing");
        cinemaHallService.add(cinemaHallNorth);

        CinemaHall cinemaHallEast = new CinemaHall();
        cinemaHallEast.setCapacity(100);
        cinemaHallEast.setDescription("East wing");
        cinemaHallService.add(cinemaHallEast);

        MovieSession movieSessionInter = new MovieSession();
        movieSessionInter.setMovie(interstellar);
        movieSessionInter.setCinemaHall(cinemaHallNorth);
        movieSessionInter.setShowTime(LocalDateTime.of(
                2020, Month.FEBRUARY, 15, 14, 20));
        movieSessionService.add(movieSessionInter);

        MovieSession movieSessionFast = new MovieSession();
        movieSessionFast.setMovie(fastAndFurious);
        movieSessionFast.setCinemaHall(cinemaHallEast);
        movieSessionFast.setShowTime(LocalDateTime.of(
                2020, Month.FEBRUARY, 15, 19, 50));
        movieSessionService.add(movieSessionFast);

        System.out.println("movie service get");
        System.out.println(movieService.get(interstellar.getId()));
        System.out.println("movieSession service get");
        System.out.println(movieSessionService.get(movieSessionInter.getId()));
        System.out.println("cinemaHallService get");
        System.out.println(cinemaHallService.get(cinemaHallEast.getId()));

        System.out.println("movieService getAll");
        movieService.getAll().forEach(System.out::println);
        System.out.println("movieSession service findAvailable");
        List<MovieSession> availableSessions = movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.of(2020, Month.FEBRUARY, 15));
        availableSessions.forEach(System.out::println);
        System.out.println("cinemaHallService getAll");
        cinemaHallService.getAll().forEach(System.out::println);
        System.out.println();

        User stepan;
        User oksana;
        User olena;

        try {
            oksana = authenticationService.register("oksana@gmail.net", "oksi19");
            stepan = authenticationService.register("stepan@ukr.net", "qwerty");
            olena = authenticationService.register("olena@ukr.net", "1234");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        try {
            authenticationService.login("olena@ukr.net", "1234");
            authenticationService.login("stepan@ukr.net", "qwerty");
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        shoppingCartService.addSession(movieSessionInter, stepan);
        shoppingCartService.addSession(movieSessionInter, olena);
        shoppingCartService.addSession(movieSessionFast, olena);
        shoppingCartService.addSession(movieSessionFast, oksana);
        shoppingCartService.addSession(movieSessionFast, stepan);

        System.out.println("\nstepan's order date: " + orderService.completeOrder(
                shoppingCartService.getByUser(stepan)).getOrderDate());
        System.out.println("\noksana's order date: " + orderService.completeOrder(
                shoppingCartService.getByUser(oksana)).getOrderDate());
        System.out.println("\nolena's order date: " + orderService.completeOrder(
                shoppingCartService.getByUser(olena)).getOrderDate());
    }
}
