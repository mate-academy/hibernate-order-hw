package mate.academy.security;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User register(String email, String password) throws RegistrationException {
        Optional<User> optionalUserByEmail = userService.findByEmail(email);
        if (optionalUserByEmail.isPresent()) {
            throw new RegistrationException("Email " + email + "already registered");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        return userService.add(newUser);
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> optionalUserByEmail = userService.findByEmail(email);
        if (optionalUserByEmail.isEmpty()) {
            throw new AuthenticationException("no user with email:" + email);
        }
        User user = optionalUserByEmail.get();
        if (user.getPassword().equals(HashUtil.hashPassword(password, user.getSalt()))) {
            return optionalUserByEmail.get();
        }

        throw new AuthenticationException("wrong password");
    }
}
