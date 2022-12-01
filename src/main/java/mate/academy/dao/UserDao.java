package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.User;

public interface UserDao extends GenericDao<User> {

    Optional<User> findByEmail(String email);
}
