package mate.academy;

import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        UserService userService = (UserService) injector.getInstance(UserService.class);

        User user = userService.findByEmail("test@example.com").get();
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);

        Order order = orderService.completeOrder(shoppingCart);
        System.out.println("Order created: " + order);

        List<Order> history = orderService.getOrdersHistory(user);
        history.forEach(System.out::println);
    }
}
