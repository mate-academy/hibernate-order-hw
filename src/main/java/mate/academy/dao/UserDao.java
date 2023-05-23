package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.User;

public interface UserDao extends AbstractDao<User> {
    Optional<User> findByEmail(String email);
}
