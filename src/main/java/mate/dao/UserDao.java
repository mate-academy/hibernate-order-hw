package mate.dao;

import java.util.Optional;
import mate.model.User;

public interface UserDao {
    User add(User user);

    Optional<User> findByEmail(String email);
}
