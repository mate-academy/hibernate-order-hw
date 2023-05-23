package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final String VALID_EMAIL_REGEX = ".+@\\w+\\.\\w+";
    @Inject
    private UserService userService;
    @Inject
    private ShoppingCartService cartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent() && isPasswordEqual(optionalUser.get(), password)) {
            return optionalUser.get();
        }
        throw new AuthenticationException("Authentication error: email or password are incorrect");
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        validateData(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userService.add(user);
        cartService.registerNewShoppingCart(user);
        return user;
    }

    private void validateData(String email, String password) throws RegistrationException {
        if (!isPasswordValid(password)) {
            throw new RegistrationException("Password must be more than "
                    + MIN_PASSWORD_LENGTH + " symbols");
        }
        if (!isEmailValid(email)) {
            throw new RegistrationException("Incorrect email address");
        }
        if (!isEmailUnique(email)) {
            throw new RegistrationException("User already registered in DB with email " + email);
        }
    }

    private boolean isEmailUnique(String email) {
        Optional<User> optionalUser = userService.findByEmail(email);
        return optionalUser.isEmpty();
    }

    private boolean isEmailValid(String email) {
        return email != null && email.matches(VALID_EMAIL_REGEX);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > MIN_PASSWORD_LENGTH;
    }

    private boolean isPasswordEqual(User user, String password) {
        String hashedPassword = HashUtil.hashPassword(password, user.getSalt());
        return user.getPassword().equals(hashedPassword);
    }
}
