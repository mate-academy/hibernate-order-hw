package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
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

        CinemaHallService cinemaHallService =
                (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
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
                (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        UserDao userDao = (UserDao) INJECTOR.getInstance(UserDao.class);
        User bob = new User();
        bob.setEmail("bob@mail.com");
        bob.setPassword("bob_password");
        userDao.add(bob);

        Ticket ticket = new Ticket();
        ticket.setMovieSession(tomorrowMovieSession);
        ticket.setUser(bob);
        ticket.setMovieSession(yesterdayMovieSession);
        ticket.setUser(bob);

        TicketDao ticketDao = (TicketDao) INJECTOR.getInstance(TicketDao.class);
        ticketDao.add(ticket);
        Ticket newTicket = new Ticket();
        ticketDao.add(newTicket);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(bob);
        shoppingCart.setTickets(List.of(ticket));
        shoppingCartService.registerNewShoppingCart(bob);

        OrderService orderService = (OrderService) INJECTOR.getInstance(OrderService.class);
        Order order = orderService.completeOrder(shoppingCart);
        System.out.println(order);
        shoppingCart.setTickets(List.of(newTicket));
        orderService.completeOrder(shoppingCart);
        shoppingCart.setTickets(List.of(ticketDao.add(new Ticket())));
        orderService.completeOrder(shoppingCart);
        shoppingCart.setTickets(List.of());
        orderService.completeOrder(shoppingCart);
        List<Order> bobOrders = orderService.getOrdersHistory(bob);
        System.out.println(bobOrders);
    }
}
