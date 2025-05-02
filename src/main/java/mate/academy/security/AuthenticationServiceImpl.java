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
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("Incorrect email or password!");
        }
        User user = optionalUser.get();
        String hashedInput = HashUtil.hashPassword(password, user.getSalt());
        if (!hashedInput.equals(user.getPasswordHash())) {
            throw new AuthenticationException("Incorrect email or password!");
        }
        return user;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with this email already exists!");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User saved = userService.add(user);
        shoppingCartService.registerNewShoppingCart(saved);
        return saved;
    }
}
