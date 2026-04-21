package mate.academy;

import java.util.List;
import java.util.Optional;
import mate.academy.lib.Injector;
import mate.academy.model.Order;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        UserService userService = (UserService) injector.getInstance(UserService.class);
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);

        Optional<User> userFromDb = userService.findByEmail("user@gmail.com");
        shoppingCartService.registerNewShoppingCart(userFromDb.orElseThrow());
        Order order = orderService.completeOrder(shoppingCartService.getByUser(userFromDb.get()));
        List<Order> orders = orderService.getOrdersHistory(userFromDb.get());
        System.out.println("Orders: " + orders);
    }
}
