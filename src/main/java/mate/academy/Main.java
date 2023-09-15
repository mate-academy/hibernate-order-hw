package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
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

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        UserService userService = (UserService) injector.getInstance(UserService.class);

        User bobUser = new User();
        bobUser.setEmail("bob@gmail.com");
        bobUser.setPassword("bobpassword");
        System.out.println(userService.add(bobUser));

        User aliceUser = new User();
        aliceUser.setEmail("alice@gmail.com");
        aliceUser.setPassword("alicepassword");
        System.out.println(userService.add(aliceUser));

        Ticket ticketBobOne = new Ticket();
        ticketBobOne.setMovieSession(yesterdayMovieSession);
        ticketBobOne.setUser(bobUser);

        Ticket ticketBobTwo = new Ticket();
        ticketBobTwo.setMovieSession(yesterdayMovieSession);
        ticketBobTwo.setUser(bobUser);

        TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);

        System.out.println(ticketDao.add(ticketBobOne));
        System.out.println(ticketDao.add(ticketBobTwo));

        Order order = new Order();
        order.setTickets(List.of(ticketBobOne, ticketBobTwo));
        order.setOrderDate(LocalDateTime.now());
        order.setUser(bobUser);

        OrderDao orderDao = (OrderDao) injector.getInstance(OrderDao.class);

        System.out.println(orderDao.add(order));

        System.out.println(orderDao.getByUser(bobUser));

        Ticket ticketAliceOne = new Ticket();
        ticketAliceOne.setMovieSession(tomorrowMovieSession);
        ticketAliceOne.setUser(aliceUser);
        System.out.println(ticketDao.add(ticketAliceOne));

        Ticket ticketAliceTwo = new Ticket();
        ticketBobTwo.setMovieSession(tomorrowMovieSession);
        ticketBobTwo.setUser(aliceUser);
        System.out.println(ticketDao.add(ticketAliceTwo));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(ticketAliceOne, ticketAliceTwo));
        shoppingCart.setUser(aliceUser);

        ShoppingCartDao shoppingCartDao =
                (ShoppingCartDao) injector.getInstance(ShoppingCartDao.class);

        System.out.println(shoppingCartDao.add(shoppingCart));

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        System.out.println(orderService.completeOrder(shoppingCart));

        System.out.println(orderService.getOrdersHistory(aliceUser));
    }
}
