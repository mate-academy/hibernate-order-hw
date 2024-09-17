package mate.academy.validation.impl;

import mate.academy.lib.Service;
import mate.academy.model.ShoppingCart;
import mate.academy.validation.ValidatorService;

@Service
public class ShoppingCartValidatorServiceImpl implements ValidatorService<ShoppingCart> {
    @Override
    public void validate(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new RuntimeException("Shopping cart can't be null");
        }
        if (shoppingCart.getTickets() == null) {
            throw new RuntimeException("Tickets can't be null");
        }
        if (shoppingCart.getTickets().isEmpty()) {
            throw new RuntimeException("Tickets in shopping cart:" + shoppingCart
                    + " cant be empty");
        }
        if (shoppingCart.getUser() == null) {
            throw new RuntimeException("User can't be null");
        }
    }
}
