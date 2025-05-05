package mate.academy;

import java.time.LocalDate;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

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

        CinemaHallService chs = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        chs.add(firstCinemaHall);
        chs.add(secondCinemaHall);

        System.out.println(chs.getAll());
        System.out.println(chs.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService mss = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        mss.add(tomorrowMovieSession);
        mss.add(yesterdayMovieSession);

        System.out.println(mss.get(yesterdayMovieSession.getId()));
        System.out.println(mss.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User user = new User();
        user.setPassword("123");
        user.setEmail("rung@gmail3.com");
        user.setSalt("kuku".getBytes());
        UserService us = (UserService) injector.getInstance(UserService.class);
        us.add(user);

        ShoppingCartService scs = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        scs.registerNewShoppingCart(user);
        scs.addSession(tomorrowMovieSession, user);
        scs.addSession(yesterdayMovieSession, user);

        OrderService os = (OrderService) injector.getInstance(OrderService.class);

        os.completeOrder(scs.getByUser(user));
        System.out.println(os.getOrdersHistory(user));
    }
}
