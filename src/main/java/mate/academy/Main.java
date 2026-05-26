package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);

        User firstUser = new User();
        User secondUser = new User();
        firstUser.setEmail("marek@gmail");
        secondUser.setEmail("jacek@gmail");

        shoppingCartService.registerNewShoppingCart(firstUser);

        orderService.completeOrder(shoppingCartService.getByUser(firstUser));

        System.out.println(orderService.getOrdersHistory(firstUser));
        System.out.println(orderService.getOrdersHistory(secondUser));




    }
}
