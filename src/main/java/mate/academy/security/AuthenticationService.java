package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

public interface AuthenticationService {
    User login(String email, String password) throws AuthenticationException;

    User register(String email, String password) throws RegistrationException;

    ShoppingCartService getShoppingCartService();
}
