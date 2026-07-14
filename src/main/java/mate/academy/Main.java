package mate.academy;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.lib.Injector;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        user.setEmail("artem@gmail.com");
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
        UserDao userDao = new UserDaoImpl();
        shoppingCart.setUser(user);
        userDao.add(user);
        shoppingCartDao.add(shoppingCart);
        Injector injector = Injector.getInstance("mate.academy");
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(shoppingCart);
        orderService.getOrdersHistory(user);
    }
}

