package mate.academy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class DataGenerator {
    private static Injector injector = Injector.getInstance("mate.academy");
    private Movie casinoRoyale;
    private Movie bulletTrain;
    private Movie maverick;
    private CinemaHall imaxHall;
    private CinemaHall hallWith4D;
    private MovieSession bulletTrainSession;
    private MovieSession bulletTrainSession1;
    private MovieSession bulletTrainSession2;
    private MovieSession maverickSession;
    private MovieSession maverickSession1;
    private MovieSession maverickSession2;
    private MovieSession casinoRoyaleSession;
    private MovieSession casinoRoyaleSession1;
    private MovieSession casinoRoyaleSession2;
    private User maxine;
    private User danice;
    private MovieService movieService;
    private CinemaHallService cinemaHallService;
    private MovieSessionService movieSessionService;
    private AuthenticationService authenticationService;
    private ShoppingCartService shoppingCartService;
    private OrderService orderService;

    public void init() {
        movieServiceGenerator();
        cinemaHallServiceGenerator();
        getUsersEmails();
        movieSessionServiceGenerator();
        try {
            registerUsers();
            loginUsers();
        } catch (RegistrationException | AuthenticationException e) {
            throw new RuntimeException("Can't use register or login user", e);
        }
        shoppingCartGenerator();
        orderGenerator();
    }

    public List<String> getUsersEmails() {
        maxine = new User();
        maxine.setEmail("maxine@gmail.com");
        maxine.setPassword("nothingPassword");
        danice = new User();
        danice.setEmail("danice@gmail.com");
        danice.setPassword("qwertyghj");
        List<String> emails = new ArrayList<>();
        emails.add(maxine.getEmail());
        emails.add(danice.getEmail());
        return emails;
    }

    private void movieGenerator() {
        casinoRoyale = new Movie();
        casinoRoyale.setTitle("007:Casino Royale");
        casinoRoyale.setDescription("First mission of James Bond");

        bulletTrain = new Movie();
        bulletTrain.setTitle("Bullet Train");
        bulletTrain.setDescription(
                "At the head of the story is a professional assassin nicknamed \"Ladybug\"");

        maverick = new Movie();
        maverick.setTitle("Top Gun: Maverick");
        maverick.setDescription(
                "Film about test pilot Captain Pete Mitchell nicknamed \"Maverick\"");
    }

    private void cinemaHallGenerator() {
        imaxHall = new CinemaHall();
        imaxHall.setCapacity(100);
        imaxHall.setDescription(
                "hall with 2 projectors which allow you to make a very high-quality picture");

        hallWith4D = new CinemaHall();
        hallWith4D.setCapacity(85);
        hallWith4D.setDescription(
                "hall where you can believe that you are in a movie as main hero");
    }

    private void movieSessionGenerator() {
        bulletTrainSession = new MovieSession();
        bulletTrainSession.setMovie(bulletTrain);
        bulletTrainSession.setCinemaHall(imaxHall);
        bulletTrainSession.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                15,
                00));

        bulletTrainSession1 = new MovieSession();
        bulletTrainSession1.setMovie(bulletTrain);
        bulletTrainSession1.setCinemaHall(imaxHall);
        bulletTrainSession1.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                18,
                00));

        bulletTrainSession2 = new MovieSession();
        bulletTrainSession2.setMovie(bulletTrain);
        bulletTrainSession2.setCinemaHall(imaxHall);
        bulletTrainSession2.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                21,
                00));

        maverickSession = new MovieSession();
        maverickSession.setMovie(maverick);
        maverickSession.setCinemaHall(hallWith4D);
        maverickSession.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                15,
                00));

        maverickSession1 = new MovieSession();
        maverickSession1.setMovie(maverick);
        maverickSession1.setCinemaHall(hallWith4D);
        maverickSession1.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                18,
                00));

        maverickSession2 = new MovieSession();
        maverickSession2.setMovie(maverick);
        maverickSession2.setCinemaHall(hallWith4D);
        maverickSession2.setShowTime(LocalDateTime.of(
                2022,
                Month.NOVEMBER,
                12,
                21,
                00));

        casinoRoyaleSession = new MovieSession();
        casinoRoyaleSession.setMovie(casinoRoyale);
        casinoRoyaleSession.setCinemaHall(imaxHall);
        casinoRoyaleSession.setShowTime(LocalDateTime.of(
                2017,
                Month.JUNE,
                5,
                12,
                15));

        casinoRoyaleSession1 = new MovieSession();
        casinoRoyaleSession1.setMovie(casinoRoyale);
        casinoRoyaleSession1.setCinemaHall(imaxHall);
        casinoRoyaleSession1.setShowTime(LocalDateTime.of(
                2017,
                Month.JUNE,
                5,
                12,
                15));

        casinoRoyaleSession2 = new MovieSession();
        casinoRoyaleSession2.setMovie(casinoRoyale);
        casinoRoyaleSession2.setCinemaHall(hallWith4D);
        casinoRoyaleSession2.setShowTime(LocalDateTime.of(
                2017,
                Month.JUNE,
                5,
                17,
                15));
    }

    private void movieServiceGenerator() {
        movieGenerator();
        movieService
                = (MovieService) injector.getInstance(MovieService.class);

        movieService.add(casinoRoyale);
        movieService.add(bulletTrain);
        movieService.add(maverick);
    }

    private void cinemaHallServiceGenerator() {
        cinemaHallGenerator();
        cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);

        cinemaHallService.add(imaxHall);
        cinemaHallService.add(hallWith4D);
    }

    private void movieSessionServiceGenerator() {
        movieSessionGenerator();
        movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(maverickSession);
        movieSessionService.add(maverickSession1);
        movieSessionService.add(maverickSession2);
        movieSessionService.add(bulletTrainSession);
        movieSessionService.add(bulletTrainSession1);
        movieSessionService.add(bulletTrainSession2);
        movieSessionService.add(casinoRoyaleSession);
        movieSessionService.add(casinoRoyaleSession1);
        movieSessionService.add(casinoRoyaleSession2);
    }

    private void registerUsers() throws RegistrationException {
        authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        try {
            authenticationService.register(maxine.getEmail(), maxine.getPassword());
        } catch (Exception e) {
            throw new RegistrationException("Can't register new user=" + maxine);
        }
        try {
            authenticationService.register(danice.getEmail(), danice.getPassword());
        } catch (Exception e) {
            throw new RegistrationException("Can't register new user=" + danice);
        }
    }

    private void loginUsers() throws AuthenticationException {
        try {
            maxine = authenticationService.login(maxine.getEmail(), maxine.getPassword());
        } catch (Exception e) {
            throw new AuthenticationException("Can't login by =" + maxine);
        }
        try {
            danice = authenticationService.login(danice.getEmail(), danice.getPassword());
        } catch (Exception e) {
            throw new AuthenticationException("Can't register new user=" + danice);
        }
    }

    private void shoppingCartGenerator() {
        shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(maverickSession, maxine);
        shoppingCartService.addSession(maverickSession2, danice);
        shoppingCartService.addSession(bulletTrainSession, maxine);
        shoppingCartService.addSession(bulletTrainSession2, danice);
    }

    private void orderGenerator() {
        orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(shoppingCartService.getByUser(maxine));
        orderService.completeOrder(shoppingCartService.getByUser(danice));
    }
}
