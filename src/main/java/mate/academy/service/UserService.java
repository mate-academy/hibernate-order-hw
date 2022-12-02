package mate.academy.service;

import java.util.Optional;
import mate.academy.model.User;

public interface UserService extends AbstractService<User> {
    Optional<User> findByEmail(String email);
}
