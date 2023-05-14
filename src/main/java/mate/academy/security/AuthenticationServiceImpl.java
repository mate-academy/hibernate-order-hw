package mate.academy.security;

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
        User user = userService.findByEmail(email).orElseThrow(
                () -> new AuthenticationException("User or password does not exist."));
        if (password == null
                || password.isEmpty()
                || !HashUtil.getInstance().hash(password, user.getSalt())
                    .equals(user.getPassword())) {
            throw new AuthenticationException("User or password does not exist.");
        }
        return user;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        validateEmail(email);
        if (password == null || password.isEmpty()) {
            throw new RegistrationException("Entered password is null or empty.");
        }
        User registeredUser = userService.add(new User(email, password));
        shoppingCartService.registerNewShoppingCart(registeredUser);
        return registeredUser;
    }

    private void validateEmail(String email) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("Email " + email + " is already registered.");
        }
    }
}
