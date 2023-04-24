package mate.academy;

import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
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
    private static final String EMAIL_1 = "1";
    private static final String EMAIL_2 = "2";
    private static final String PASSWORD_1 = "1";
    private static final String PASSWORD_2 = "2";
    private static final Injector injector
            = Injector.getInstance("mate.academy");
    private static final MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final UserService userService
            = (UserService) injector.getInstance(UserService.class);
    private static final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);
    private static final long FIRST_ID = 1L;
    private static final long SECOND_ID = 2L;

    public static void main(String[] args) {
        initializeMovies();
        initializeCinemaHalls();
        initializeMovieSessions();
        initializeUsers();
        testShoppingCartService();
        testRegistration();
        testOrders();
    }

    private static void testOrders() {
        User user = userService.findByEmail(EMAIL_1).get();
        shoppingCartService.addSession(movieSessionService.get(FIRST_ID), user);
        shoppingCartService.addSession(movieSessionService.get(SECOND_ID), user);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        orderService.completeOrder(shoppingCart);
        //expected filled order
        System.out.println(orderService.getOrdersHistory(user));
        //expected empty cart
        System.out.println(shoppingCartService.getByUser(user).getTickets());
    }

    private static void testShoppingCartService() {
        User user = userService.findByEmail(EMAIL_1).get();
        System.out.println(shoppingCartService.getByUser(user));
        //expect no tickets in shopping cart
        shoppingCartService.addSession(movieSessionService.get(FIRST_ID), user);
        shoppingCartService.addSession(movieSessionService.get(SECOND_ID), user);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        System.out.println(shoppingCart.getTickets());
        //expect two tickets in shopping cart
        shoppingCartService.clearShoppingCart(shoppingCart);
        System.out.println(shoppingCartService.getByUser(user));
        //expect zero tickets in shopping cart

    }

    private static void initializeMovies() {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        Movie fastAndFurious2 = new Movie("Fast and Furious2");
        fastAndFurious.setDescription("An action film about more street racing,"
                + " heists, and spies.");
        movieService.add(fastAndFurious2);

        Movie fastAndFurious3 = new Movie("Fast and Furious3");
        fastAndFurious.setDescription("An action film about more street racing,"
                + " more heists, and spies.");
        movieService.add(fastAndFurious3);
    }

    private static void initializeCinemaHalls() {
        CinemaHall normalHall = new CinemaHall();
        normalHall.setCapacity(100);
        normalHall.setDescription("regular hall");
        cinemaHallService.add(normalHall);

        CinemaHall vipHall = new CinemaHall();
        vipHall.setCapacity(20);
        vipHall.setDescription("VIP hall");
        cinemaHallService.add(vipHall);
    }

    private static void initializeMovieSessions() {

        MovieSession pastShowingPart2 = new MovieSession();
        pastShowingPart2.setCinemaHall(cinemaHallService.get(FIRST_ID));
        pastShowingPart2.setMovie(movieService.get(FIRST_ID));
        pastShowingPart2.setShowTime(LocalDateTime.now().minusDays(1));
        movieSessionService.add(pastShowingPart2);

        MovieSession regularShowingPart2 = new MovieSession();
        regularShowingPart2.setCinemaHall(cinemaHallService.get(FIRST_ID));
        regularShowingPart2.setMovie(movieService.get(FIRST_ID));
        regularShowingPart2.setShowTime(LocalDateTime.now());
        movieSessionService.add(regularShowingPart2);

        MovieSession vipShowingPart3 = new MovieSession();
        vipShowingPart3.setCinemaHall(cinemaHallService.get(SECOND_ID));
        vipShowingPart3.setMovie(movieService.get(SECOND_ID));
        vipShowingPart3.setShowTime(LocalDateTime.now().plusHours(3));
        movieSessionService.add(vipShowingPart3);

        MovieSession futureShowingPart2 = new MovieSession();
        futureShowingPart2.setCinemaHall(cinemaHallService.get(SECOND_ID));
        futureShowingPart2.setMovie(movieService.get(FIRST_ID));
        futureShowingPart2.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(futureShowingPart2);

        MovieSession futureVipShowingPart2 = new MovieSession();
        futureVipShowingPart2.setCinemaHall(cinemaHallService.get(SECOND_ID));
        futureVipShowingPart2.setMovie(movieService.get(FIRST_ID));
        futureVipShowingPart2.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(futureVipShowingPart2);
    }

    private static void initializeUsers() {
        try {
            authenticationService.register(EMAIL_1, PASSWORD_1);
            authenticationService.register(EMAIL_2, PASSWORD_2);
        } catch (RegistrationException e) {
            throw new RuntimeException("Exception in user initialization");
        }
    }

    private static void testRegistration() {
        try {
            authenticationService.register(EMAIL_1, null);
        } catch (RegistrationException e) {
            System.out.println("failed as expected");
        }

        try {
            authenticationService.register(EMAIL_1, "");
        } catch (RegistrationException e) {
            System.out.println("failed as expected");
        }

        try {
            authenticationService.register(EMAIL_1, PASSWORD_1);
            authenticationService.login(EMAIL_1, PASSWORD_1);
            authenticationService.register(EMAIL_2, PASSWORD_1);
        } catch (RegistrationException | AuthenticationException e) {
            throw new RuntimeException("Supposed to work without throwing an exception");
        }

        try {
            authenticationService.register(EMAIL_1, PASSWORD_1);
        } catch (RegistrationException e) {
            System.out.println("failed as expected");
        }
        try {
            authenticationService.login(EMAIL_1, PASSWORD_2);
        } catch (AuthenticationException e) {
            System.out.println("failed as expected");
        }
    }
}
