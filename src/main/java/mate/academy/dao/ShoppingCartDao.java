package mate.academy.dao;

import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartDao extends AbstractDao<ShoppingCart> {
    ShoppingCart getByUser(User user);

    void update(ShoppingCart shoppingCart);
}
