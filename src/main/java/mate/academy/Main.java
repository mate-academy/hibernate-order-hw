package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) {
        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("1345");

        Movie film = new Movie("Ford v. Ferrari");
        film.setDescription("The film tells the true story of the "
                + "struggle between the Ford and Ferrari teams that erupted at "
                + "the 1966 Le Mans race.");
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(film);

        CinemaHall cinemaHall = (CinemaHall) injector.getInstance(CinemaHall.class);
        cinemaHall.setCapacity(150);
        cinemaHall.setDescription("Main Hall");

        MovieSession movieSession = (MovieSession) injector.getInstance(MovieSession.class);
        movieSession.setMovie(film);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));

        ShoppingCartService service = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        service.registerNewShoppingCart(bob);
        service.addSession(movieSession, bob);

        ShoppingCart shoppingCart = service.getByUser(bob);
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        orderService.completeOrder(shoppingCart);
        orderService.getOrderHistory(bob);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = (CinemaHall) injector.getInstance(CinemaHall.class);
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = (CinemaHall) injector.getInstance(CinemaHall.class);
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = (MovieSession)
                injector.getInstance(MovieSession.class);
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = (MovieSession)
                injector.getInstance(MovieSession.class);
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService)
                injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));
    }
}
