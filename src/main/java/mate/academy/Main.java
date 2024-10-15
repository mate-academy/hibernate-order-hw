package mate.academy;

import mate.academy.exception.AuthenticationException;
import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.OrderService;

public class Main {
    private static final Injector injector =
            Injector.getInstance("mate.academy");

    public static void main(String[] args) throws AuthenticationException {
        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User register = authenticationService.login("bob@gmail.com", "1234");
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        System.out.println(orderService.getOrderHistory(register));
    }
}
