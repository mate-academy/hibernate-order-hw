package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.dao.CinemaHallDao;
import mate.academy.dao.MovieDao;
import mate.academy.dao.MovieSessionDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.CinemaHallDaoImpl;
import mate.academy.dao.impl.MovieDaoImpl;
import mate.academy.dao.impl.MovieSessionDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
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
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        User user = new User();
        user.setEmail("super_user@ukr.net");
        user.setPassword("Victory");
        UserDao userDao = new UserDaoImpl();
        userDao.add(user);

        Movie movie = new Movie();
        movie.setTitle("Dovbush");
        movie.setTitle("Ukrainian historical drama");
        MovieDao movieDao = new MovieDaoImpl();
        movieDao.add(movie);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setDescription("IMAX");
        cinemaHall.setCapacity(150);
        CinemaHallDao cinemaHallDao = new CinemaHallDaoImpl();
        cinemaHallDao.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.parse("2023-09-23T21:00:00.000"));
        MovieSessionDao movieSessionDao = new MovieSessionDaoImpl();
        movieSessionDao.add(movieSession);

        ShoppingCartService shoppingCartService = (ShoppingCartService) INJECTOR
                .getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(movieSession, user);
        ShoppingCart cartByUser = shoppingCartService.getByUser(user);

        //Testing Order Service
        OrderService orderService = (OrderService) INJECTOR.getInstance(OrderService.class);
        System.out.println(orderService.completeOrder(cartByUser));
        System.out.println(orderService.getOrdersHistory(user));
        //Processed with OK

        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);
        ;

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
    }
}
