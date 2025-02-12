package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.impl.MovieServiceImpl;
import mate.academy.service.impl.OrderServiceImpl;
import mate.academy.service.impl.ShoppingCartServiceImpl;

public class Main {
    public static void main(String[] args) throws RegistrationException, AuthenticationException {

        Injector injector = Injector.getInstance("mate.academy");

        MovieService movieService = (MovieService) injector.getInstance(MovieServiceImpl.class);
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

        CinemaHallService cinemaHallService;
        cinemaHallService = (CinemaHallService) injector.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService;
        movieSessionService = (MovieSessionService) injector.getInstance(MovieService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        // Створення сервісів
        AuthenticationService authenticationService = new AuthenticationServiceImpl();
        // Реєстрація та авторизація користувача
        User user = authenticationService.register("andriy@gmail.com", "1234");

        // Створення квитка
        Ticket ticket = new Ticket();
        ticket.setMovieSession(yesterdayMovieSession);
        ticket.setUser(user);

        TicketDao ticketDao = new TicketDaoImpl();
        ticketDao.add(ticket);

        // Створення кошика покупок
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(ticket));
        shoppingCart.setUser(user);

        // Реєстрація нового кошика покупок
        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();
        shoppingCartService.registerNewShoppingCart(user);

        // Додавання сеансу до кошика
        shoppingCartService.addSession(yesterdayMovieSession, user);

        // Завершення замовлення
        OrderService orderService = new OrderServiceImpl();
        orderService.completeOrder(shoppingCartService.getByUser(user));

        // Отримання історії замовлень
        orderService.getOrdersHistory(user);
    }
}
