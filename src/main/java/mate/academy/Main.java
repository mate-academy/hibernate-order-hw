package mate.academy;

import java.time.LocalDate;
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

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    public static void main(String[] args) {
        //--------Movies--------//
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));

        Movie interstellar = new Movie("Interstellar");
        interstellar.setDescription("When Earth becomes uninhabitable in the future, "
                + "a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, "
                + "along with a team of researchers, to find a new planet for humans.");
        movieService.add(interstellar);

        Movie tronLegacy = new Movie("Tron: Legacy");
        tronLegacy.setDescription("Sam misses his father, a virtual world designer, "
                + "and enters a virtual space that has become much "
                + "more dangerous than his father intended. Now, both "
                + "father and son embark upon a life-and-death journey.");
        movieService.add(tronLegacy);

        Movie inception = new Movie("Inception");
        inception.setDescription("Cobb steals information from his targets by entering "
                + "their dreams. Saito offers to wipe clean Cobb's criminal history "
                + "as payment for performing an inception on his sick competitor's son.");
        movieService.add(inception);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        //--------Cinema halls--------//
        CinemaHall redCinemaHall = new CinemaHall(50);
        redCinemaHall.setDescription("Red cinema hall with comfy chairs");
        cinemaHallService.add(redCinemaHall);

        CinemaHall greenCinemaHall = new CinemaHall(60);
        greenCinemaHall.setDescription("Green cinema hall with big screen chairs");
        cinemaHallService.add(greenCinemaHall);

        CinemaHall blueCinemaHall = new CinemaHall(70);
        blueCinemaHall.setDescription("blue cinema hall with comfy chairs, big screen and IMAX");
        cinemaHallService.add(blueCinemaHall);
        cinemaHallService.get(blueCinemaHall.getId());
        cinemaHallService.getAll().forEach(System.out::println);

        //--------Movie sessions--------//
        MovieSession movieSession1 = new MovieSession(interstellar, blueCinemaHall,
                LocalDateTime.now().plusMinutes(5).plusHours(3).plusDays(5));
        movieSessionService.add(movieSession1);

        MovieSession movieSession2 = new MovieSession(interstellar, blueCinemaHall,
                LocalDateTime.now().plusMinutes(10).plusHours(6).plusDays(5));
        movieSessionService.add(movieSession2);

        MovieSession movieSession3 = new MovieSession(inception, greenCinemaHall,
                LocalDateTime.now().plusMinutes(30).plusHours(5).plusDays(2));
        movieSessionService.add(movieSession3);

        MovieSession movieSession4 = new MovieSession(tronLegacy, blueCinemaHall,
                LocalDateTime.now().plusMinutes(15).plusHours(5).plusDays(5));
        movieSessionService.add(movieSession4);

        MovieSession movieSession5 = new MovieSession(fastAndFurious, redCinemaHall,
                LocalDateTime.now().plusMinutes(5).plusHours(5).plusDays(5));
        movieSessionService.add(movieSession5);

        movieSessionService.get(movieSession3.getId());
        movieSessionService.findAvailableSessions(interstellar.getId(),
                LocalDate.now().plusDays(5)).forEach(System.out::println);

        //--------Authentication--------//
        User bobRoss = new User("bobross@gmail.com", "123456789s");
        User nancySmith = new User("nancy@gmail.com", "qwer4rfdx");
        User honorMeadows = new User("honor@gmail.com", "8r2fWQF@#$@@");
        User myleeSimmons = new User("myleesimmons@mail.com", "@F(F3fu2uf23g23g");
        User elouiseAustin = new User("elouise-austin", "0932f929ffd");
        User john = new User("john@gmail.com", "12441dsaf");
        try {
            bobRoss = authenticationService
                    .register(bobRoss.getEmail(), bobRoss.getPassword());
            nancySmith = authenticationService
                    .register(nancySmith.getEmail(), nancySmith.getPassword());
            honorMeadows = authenticationService
                    .register(honorMeadows.getEmail(), honorMeadows.getPassword());
            myleeSimmons = authenticationService
                    .register(myleeSimmons.getEmail(), myleeSimmons.getPassword());
            elouiseAustin = authenticationService
                    .register(elouiseAustin.getEmail(), elouiseAustin.getPassword());
            bobRoss = authenticationService.register(bobRoss.getEmail(), bobRoss.getPassword());
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }
        try {
            authenticationService.login(bobRoss.getEmail(), bobRoss.getPassword());
            authenticationService.login(john.getEmail(), john.getPassword());
            authenticationService.login(nancySmith.getEmail(), nancySmith.getPassword());
            authenticationService.login(honorMeadows.getEmail(), honorMeadows.getPassword());
            authenticationService.login(myleeSimmons.getEmail(), myleeSimmons.getPassword());
            authenticationService.login(elouiseAustin.getEmail(), elouiseAustin.getPassword());
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        //--------Buying tickets--------//
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(movieSession1, bobRoss);
        shoppingCartService.addSession(movieSession3, bobRoss);

        ShoppingCart bobRossShoppingCart = shoppingCartService.getByUser(bobRoss);
        System.out.println(bobRossShoppingCart);
        orderService.completeOrder(bobRossShoppingCart);

        shoppingCartService.addSession(movieSession4, nancySmith);
        shoppingCartService.addSession(movieSession5, nancySmith);
        ShoppingCart nancySmithShoppingCart = shoppingCartService.getByUser(nancySmith);
        orderService.completeOrder(nancySmithShoppingCart);

        shoppingCartService.addSession(movieSession1, honorMeadows);
        ShoppingCart honorMeadowsShoppingCart = shoppingCartService.getByUser(honorMeadows);
        orderService.completeOrder(honorMeadowsShoppingCart);

        shoppingCartService.addSession(movieSession3, myleeSimmons);
        shoppingCartService.addSession(movieSession5, myleeSimmons);
        ShoppingCart myleeSimmonsShoppingCart = shoppingCartService.getByUser(myleeSimmons);
        orderService.completeOrder(myleeSimmonsShoppingCart);

        shoppingCartService.addSession(movieSession1, elouiseAustin);
        ShoppingCart elouiseAustinShoppingCart = shoppingCartService.getByUser(elouiseAustin);
        orderService.completeOrder(elouiseAustinShoppingCart);

        System.out.println(orderService.getOrdersHistory(bobRoss));
        System.out.println(orderService.getOrdersHistory(nancySmith));
        System.out.println(orderService.getOrdersHistory(honorMeadows));
        System.out.println(orderService.getOrdersHistory(myleeSimmons));
        System.out.println(orderService.getOrdersHistory(elouiseAustin));
    }
}
