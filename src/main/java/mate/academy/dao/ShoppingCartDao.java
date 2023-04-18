package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartDao {
    ShoppingCart add(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(User user);

    void update(ShoppingCart shoppingCart);
}
