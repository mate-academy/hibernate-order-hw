package mate.academy.dao;

import java.util.List;
import mate.academy.model.Order;
import mate.academy.model.User;

public interface OrderDao {
    Order add(Order order); // стандартний метод для збереження

    List<Order> getByUser(User user); // метод для отримання історії замовлень певного користувача
}

