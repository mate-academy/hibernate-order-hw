package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    public static void main(String[] args) {
        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

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

        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        final AuthenticationService authService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        System.out.println("\n--- TEST ZAMÓWIEŃ I KOSZYKA ---");

        User user = null;
        try {
            user = authService.register("bob@gmail.com", "1234");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Zarejestrowano użytkownika: " + user.getEmail());

        shoppingCartService.addSession(tomorrowMovieSession, user);
        shoppingCartService.addSession(tomorrowMovieSession, user);
        ShoppingCart bobsCart = shoppingCartService.getByUser(user);
        System.out.println("Koszyk użytkownika przed zamówieniem (liczba biletów): "
                + bobsCart.getTickets().size());
        bobsCart.getTickets().forEach(System.out::println);
        Order order = orderService.completeOrder(bobsCart);
        System.out.println("Zamówienie zostało pomyślnie złożone! ID: " + order.getId());
        ShoppingCart bobsCartAfterOrder = shoppingCartService.getByUser(user);
        System.out.println("Koszyk po złożeniu zamówienia (powinien być pusty): "
                + bobsCartAfterOrder.getTickets().size());
        System.out.println("\n--- HISTORIA ZAMÓWIEŃ ---");
        List<Order> ordersHistory = orderService.getOrdersHistory(user);

        for (Order orderFromHistory : ordersHistory) {
            System.out.println("Zamówienie z dnia: " + orderFromHistory.getOrderDate());
            System.out.println("Kupione bilety w tym zamówieniu:");
            orderFromHistory.getTickets().forEach(ticket ->
                    System.out.println(" - Bilet na film: "
                            + ticket.getMovieSession().getMovie().getTitle())
            );
        }
    }
}
