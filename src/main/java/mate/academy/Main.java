package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import mate.academy.service.UserService;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        Movie bigFish = new Movie("BIG FISH");
        bigFish.setDescription("This is the movie about Big Dream");
        Movie yesterdayMovie = new Movie("Yesterday Movie");
        yesterdayMovie.setDescription("This is the movie about Yesterday");

        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);
        movieService.add(fastAndFurious);
        movieService.add(bigFish);
        movieService.add(yesterdayMovie);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHall vipCinemaHall = new CinemaHall();
        vipCinemaHall.setCapacity(10);
        vipCinemaHall.setDescription("VIP hall with capacity 10 "
                + "and possibility to get some drinks!");

        CinemaHallService cinemaHallService
                = (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);
        cinemaHallService.add(vipCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(vipCinemaHall);
        todayMovieSession.setMovie(bigFish);
        todayMovieSession.setShowTime(LocalDateTime.now());

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(secondCinemaHall);
        yesterdayMovieSession.setMovie(yesterdayMovie);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService
                = (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);
        movieSessionService.add(todayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        bigFish.getId(), LocalDate.now()));

        System.out.println("*** Users tests ***");

        AuthenticationService authenticationService
                = (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);
        authenticationService.register("andrew@gmail.com", "Pass1234");
        authenticationService.register("kate@gmail.com", "Pass1234");

        UserService userService = (UserService) INJECTOR.getInstance(UserService.class);
        User kate = userService.findByEmail("kate@gmail.com").get();

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);

        System.out.println(shoppingCartService.getByUser(kate));

        shoppingCartService.addSession(todayMovieSession, kate);
        ShoppingCart kateShoppingKartToday = shoppingCartService.getByUser(kate);
        System.out.println(kateShoppingKartToday);

        System.out.println("*** Orders tests ***");

        OrderService orderService = (OrderService) INJECTOR.getInstance(OrderService.class);
        Order order = orderService.completeOrder(kateShoppingKartToday);
        System.out.println(order);

        shoppingCartService.addSession(yesterdayMovieSession, kate);
        ShoppingCart kateShoppingKartYesterday = shoppingCartService.getByUser(kate);

        System.out.println("kateShoppingKartYesterday before complete newOrder"
                + kateShoppingKartYesterday);

        Order newOrder = orderService.completeOrder(kateShoppingKartYesterday);

        orderService.getOrdersHistory(kate).forEach(System.out::println);
        System.out.println("kateShoppingKartYesterday after complete newOrder"
                + kateShoppingKartYesterday);
    }
}
