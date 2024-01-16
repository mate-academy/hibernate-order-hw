package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import mate.academy.dao.OrderDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private ShoppingCartService shoppingCartService;
    @Inject
    private OrderDao orderDao;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        User shoppingCartUserFromDb = shoppingCart.getUser();
        ShoppingCart shoppingCartFromDb = shoppingCartService
                .getByUser(shoppingCartUserFromDb);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(new ArrayList<>(shoppingCartFromDb.getTickets()));
        order.setUser(shoppingCartUserFromDb);

        orderDao.add(order);
        shoppingCartService.clearShoppingCart(shoppingCartFromDb);
        return order;
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("from Order o"
                    + " inner join o.tickets"
                    + " where o.user = :user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        }
    }
}
