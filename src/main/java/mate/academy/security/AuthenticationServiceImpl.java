package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;
import org.apache.commons.lang3.StringUtils;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User byEmail = userService.findByEmail(email).orElseThrow(() ->
                new AuthenticationException("User with : <" + email + "> and <"
                        + password + "> don't passed authentication process"));
        if (!byEmail.getPassword()
                .equals(HashUtil.hashPassword(password, byEmail.getSalt()))) {
            throw new AuthenticationException(" For User with ID <" + byEmail.getId()
                    + "> password was inputting incorrectly");
        }
        return byEmail;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new RegistrationException("Email and password should be not null or empty");
        }
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with this email already exist");
        }
        User user = new User(email, password);
        byte[] salt = HashUtil.getSalt();
        user.setSalt(salt);
        user.setPassword(HashUtil.hashPassword(user.getPassword(), salt));
        shoppingCartService.registerNewShoppingCart(user);
        return userService.add(user);
    }
}
