package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public static void main(String[] args) throws RegistrationException {
        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        /*movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);*/

        List<Movie> allMovies = movieService.getAll();

        Optional<Movie> optionalMovie = allMovies.stream()
                .filter(movie -> movie.getTitle().equals("Fast and Furious"))
                .findFirst();

        if (optionalMovie.isPresent()) {
            fastAndFurious = optionalMovie.get();
        } else {
            fastAndFurious = new Movie("Fast and Furious");
            fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
            movieService.add(fastAndFurious);
        }

        List<Movie> list = List.of(fastAndFurious);
        System.out.println(list);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        //
        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);

        //List<CinemaHall> cinemaHallList = cinemaHallService.getAll();

        /*  System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));*/

        //firstCinemaHall
        Optional<CinemaHall> optionalFirstHall = cinemaHallService.getAll().stream()
                .filter(cinemaFirstHall ->
                        cinemaFirstHall.getDescription().equals("first hall with capacity 100"))
                .findFirst();

        if (optionalFirstHall.isPresent()) {
            firstCinemaHall = optionalFirstHall.get();
        } else {
            firstCinemaHall = new CinemaHall();
            firstCinemaHall.setCapacity(100);
            firstCinemaHall.setDescription("first hall with capacity 100");
            cinemaHallService.add(firstCinemaHall);
        }

        //secondCinemaHall
        Optional<CinemaHall> optionalCinemaSecondHall = cinemaHallService.getAll().stream()
                .filter(cinemaSecondHall ->
                        cinemaSecondHall.getDescription().equals("second hall with capacity 200"))
                .findFirst();

        if (optionalCinemaSecondHall.isPresent()) {
            secondCinemaHall = optionalCinemaSecondHall.get();
        } else {
            secondCinemaHall = new CinemaHall();
            secondCinemaHall.setCapacity(200);
            secondCinemaHall.setDescription("second hall with capacity 200");
            cinemaHallService.add(secondCinemaHall);
        }

        System.out.println(firstCinemaHall);
        System.out.println(secondCinemaHall);

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        OrderService orderService =
                (OrderService) injector.getInstance(OrderService.class);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        //Класи-сутності
        User user = authenticationService.register("user123@gmail.com", "12345");

        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);

        Order order = orderService.completeOrder(shoppingCart);
        System.out.println("Order placed: " + order);

        System.out.println("Order history: ");
        orderService.getOrdersHistory(user).forEach(System.out::println);
    }
}
