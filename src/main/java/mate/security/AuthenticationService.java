package mate.security;

import mate.exception.AuthenticationException;
import mate.exception.RegistrationException;
import mate.model.User;

public interface AuthenticationService {
    User login(String email, String password) throws AuthenticationException;

    User register(String email, String password) throws RegistrationException;
}
