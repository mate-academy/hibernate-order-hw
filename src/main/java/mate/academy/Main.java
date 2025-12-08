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

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector
                .getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing.");
        movieService.add(fastAndFurious);
        System.out.println("Added movie: " + movieService.get(fastAndFurious.getId()));

        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        cinemaHallService.add(firstCinemaHall);

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(secondCinemaHall);

        System.out.println("All cinema halls: " + cinemaHallService.getAll());

        final MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setCinemaHall(firstCinemaHall);
        tomorrowSession.setMovie(fastAndFurious);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1L));
        movieSessionService.add(tomorrowSession);

        MovieSession todaySession = new MovieSession();
        todaySession.setCinemaHall(secondCinemaHall);
        todaySession.setMovie(fastAndFurious);
        todaySession.setShowTime(LocalDateTime.now());
        movieSessionService.add(todaySession);

        System.out.println("Available sessions: "
                + movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);

        User user = registerUser(authenticationService);

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(tomorrowSession, user);
        shoppingCartService.addSession(todaySession, user);

        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        System.out.println("Shopping cart: " + shoppingCart);
        System.out.println("Tickets in cart: " + shoppingCart.getTickets().size());

        OrderService orderService = (OrderService) injector
                .getInstance(OrderService.class);

        Order completedOrder = orderService.completeOrder(shoppingCart);
        System.out.println("Completed order: " + completedOrder);
        System.out.println("Order ID: " + completedOrder.getId());
        System.out.println("Order date: " + completedOrder.getOrderDate());
        System.out.println("Tickets in order: " + completedOrder.getTickets().size());

        ShoppingCart clearedCart = shoppingCartService.getByUser(user);
        System.out.println("Shopping cart after order: "
                + clearedCart.getTickets().size() + " tickets");

        System.out.println("\n=== Order History ===");
        orderService.getOrdersHistory(user).forEach(order -> {
            System.out.println("Order #" + order.getId()
                    + " - Date: " + order.getOrderDate()
                    + " - Tickets: " + order.getTickets().size());
        });

        MovieSession anotherSession = new MovieSession();
        anotherSession.setCinemaHall(firstCinemaHall);
        anotherSession.setMovie(fastAndFurious);
        anotherSession.setShowTime(LocalDateTime.now().plusDays(2L));
        movieSessionService.add(anotherSession);

        shoppingCartService.addSession(anotherSession, user);
        Order secondOrder = orderService.completeOrder(
                shoppingCartService.getByUser(user));
        System.out.println("\nSecond order created: " + secondOrder.getId());

        System.out.println("\n=== Final Order History ===");
        orderService.getOrdersHistory(user).forEach(order -> {
            System.out.println("Order #" + order.getId()
                    + " - Tickets: " + order.getTickets().size());
        });
    }

    private static User registerUser(AuthenticationService authenticationService) {
        try {
            User user = authenticationService.register("user@example.com", "password123");
            System.out.println("Registered user: " + user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed", e);
        }
    }
}
