package mate.academy;

import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
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
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        User bob = userService.findByEmail("bob@gmail").get();
        // shoppingCartService.registerNewShoppingCart(bob);
        MovieSession yesterdayMovieSession = movieSessionService.get(1L);
        MovieSession tomorrowMovieSession = movieSessionService.get(2L);
        // shoppingCartService.addSession(yesterdayMovieSession,bob);
        // shoppingCartService.addSession(tomorrowMovieSession,bob);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(bob);

        //System.out.println(orderService.completeOrder(shoppingCart));
        System.out.println(orderService.getOrdersHistory(bob));
    }
}
