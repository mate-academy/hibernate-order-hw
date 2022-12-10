package mate.academy;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.UserService;

public class Main {
    private static Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        DataGenerator dataGenerator;
        MovieService movieService
                = (MovieService) injector.getInstance(MovieService.class);
        List<Movie> movieList = movieService.getAll();
        System.out.println("Movies");
        movieList.forEach(System.out::println);
        System.out.println();
        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        List<CinemaHall> cinemaHallList = cinemaHallService.getAll();
        System.out.println("Cinema halls");
        cinemaHallList.forEach(System.out::println);
        System.out.println();
        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        System.out.println("Movie sessions on 12th November 2022");
        for (long i = 1; i <= movieList.size(); i++) {
            List<MovieSession> movieSessionList
                    = movieSessionService.findAvailableSessions(
                    i,
                    LocalDate.of(2022, Month.NOVEMBER, 12));
            movieSessionList.forEach(System.out::println);
        }
        System.out.println();
        List<String> emails = DataGenerator.getUsersEmails();
        List<User> users = new ArrayList<>();
        UserService userService = (UserService) injector.getInstance(UserService.class);
        for (String email : emails) {
            users.add(userService.findByEmail(email).get());
        }
        OrderService orderService
                = (OrderService) injector.getInstance(OrderService.class);
        List<Order> orderList = new ArrayList<>();
        for (User user : users) {
            orderList.addAll(orderService.getOrdersHistory(user));
        }
        System.out.println("Order history of all users");
        orderList.forEach(System.out::println);
    }

}
