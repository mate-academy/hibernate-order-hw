package mate.service;

import java.util.Optional;
import mate.model.User;

public interface UserService {
    User add(User user);

    Optional<User> findByEmail(String email);
}
