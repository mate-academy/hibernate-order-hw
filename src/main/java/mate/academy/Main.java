package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import mate.academy.service.ShoppingCartService;
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

        CinemaHallService cinemaHallService = (CinemaHallService) injector
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

        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now().plusDays(1)).forEach(System.out::println);
        System.out.println("\n--- MovieSession Testing Complete ---\n");

        // 4. Create and test User
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        userService.add(testUser);
        System.out.println("Added User: " + userService.findByEmail("test@example.com")
                .orElse(null) + "\n");

        // 5. Create and test ShoppingCart
        // Registering a new shopping cart is often implicit with user creation or first access.
        // We need to fetch the existing cart or create a new one if it doesn't exist.
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        ShoppingCart userCart = shoppingCartService.getByUser(testUser);
        System.out.println("User's Shopping Cart (initially): " + userCart + "\n");

        // 6. Create Tickets and add to ShoppingCart
        TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        Ticket ticket1 = new Ticket();
        ticket1.setMovieSession(tomorrowMovieSession);
        ticket1.setUser(testUser);
        ticketDao.add(ticket1); // Persist the ticket first

        Ticket ticket2 = new Ticket();
        ticket2.setMovieSession(tomorrowMovieSession);
        ticket2.setUser(testUser);
        ticketDao.add(ticket2); // Persist the second ticket

        // --- SHOPPING CART SERVICE (addSession) TESTING ---
        System.out.println("--- Testing ShoppingCartService (addSession) ---\n");
        // Add a movie session to the shopping cart
        shoppingCartService.registerNewShoppingCart(testUser);
        shoppingCartService.addSession(tomorrowMovieSession, testUser);
        System.out.println("User's Shopping Cart after adding session: "
                + shoppingCartService.getByUser(testUser) + "\n");

        // Add another movie session
        shoppingCartService.addSession(yesterdayMovieSession, testUser);
        System.out.println("User's Shopping Cart after adding another session: "
                + shoppingCartService.getByUser(testUser) + "\n");

        System.out.println("\n--- ShoppingCartService Testing Complete ---\n");

        // --- ORDER SERVICE TESTING ---
        System.out.println("--- Testing OrderService ---\n");

        // Test completeOrder(ShoppingCart shoppingCart)
        System.out.println("Completing order for user: " + testUser.getEmail());
        // Retrieve the latest state of the shopping cart before completing the order
        ShoppingCart currentCart = shoppingCartService.getByUser(testUser);
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        Order completedOrder = orderService.completeOrder(currentCart);
        System.out.println("Order completed: " + completedOrder + "\n");
        System.out.println("User's Shopping Cart after order completion (should be empty): "
                + shoppingCartService.getByUser(testUser) + "\n");

        // Test getOrderHistory(User user)
        System.out.println("Getting order history for user: " + testUser.getEmail());
        List<Order> orderHistory = orderService.getOrderHistory(testUser);
        if (!orderHistory.isEmpty()) {
            System.out.println("Order History:");
            orderHistory.forEach(order -> {
                System.out.println("  Order ID: " + order.getId() + ", Date: "
                        + order.getLocalDateTime() + ", Tickets: " + order.getTickets().size());
                order.getTickets().forEach(ticket -> System.out.println("    - Ticket ID: "
                        + ticket.getId() + ", Movie Session ID: "
                        + ticket.getMovieSession().getId()));
            });
        } else {
            System.out.println("No order history found for " + testUser.getEmail());
        }
        System.out.println("\n--- OrderService Testing Complete ---");
    }
}
