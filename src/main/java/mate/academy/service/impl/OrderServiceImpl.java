package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.OrderDao;
import mate.academy.dao.impl.OrderDaoImpl;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setTickets(shoppingCart.getTickets());
        order.setOrderDate(LocalDateTime.now());
        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();
        shoppingCartService.clearShoppingCart(shoppingCart);
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order o WHERE o.user =: user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
        }
    }
}
