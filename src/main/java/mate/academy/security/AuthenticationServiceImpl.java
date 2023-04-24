package mate.academy.security;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> user = userService.findByEmail(email);
        if (password == null
                || password.isEmpty()
                || user.isEmpty()
                || !HashUtil.getInstance().hash(password, user.get().getSalt())
                .equals(user.get().getPassword())) {
            throw new AuthenticationException("User or password does not exist.");
        }
        return user.get();
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        validateEmail(email);
        validatePassword(password);
        User registeredUser = userService.add(new User(email, password));
        shoppingCartService.registerNewShoppingCart(registeredUser);
        return registeredUser;
    }

    private void validateEmail(String email) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("Email " + email + " is already registered.");
        }
    }

    private void validatePassword(String password) throws RegistrationException {
        if (password == null || password.isEmpty()) {
            throw new RegistrationException("Entered password is null or empty.");
        }
    }
}
