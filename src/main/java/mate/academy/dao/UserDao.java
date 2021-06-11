package mate.academy.dao;

import java.util.Optional;
import mate.academy.lib.Dao;
import mate.academy.model.User;

@Dao
public interface UserDao {
    User add(User user);

    Optional<User> findByEmail(String email);
}
