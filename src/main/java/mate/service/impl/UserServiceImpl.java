package mate.service.impl;

import java.util.Optional;
import mate.dao.UserDao;
import mate.lib.Inject;
import mate.lib.Service;
import mate.model.User;
import mate.service.UserService;
import mate.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private UserDao userDao;

    @Override
    public User add(User user) {
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt()));
        return userDao.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
