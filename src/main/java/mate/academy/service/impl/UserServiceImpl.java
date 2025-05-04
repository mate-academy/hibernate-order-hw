package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private UserDao userDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public User add(User user) {
        encryptPassword(user);
        userDao.add(user);
        shoppingCartService.registerNewShoppingCart(user);

        return userDao.get(user.getEmail()).orElseThrow(
                () -> new RegistrationException(
                        "registration failed, " + user + " not in database"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.get(email);
    }

    private User encryptPassword(User user) {
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt()));
        return user;
    }
}
