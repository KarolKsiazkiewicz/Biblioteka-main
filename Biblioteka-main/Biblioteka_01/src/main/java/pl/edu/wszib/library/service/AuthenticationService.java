package pl.edu.wszib.library.service;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.library.core.IAuthenticationService;
import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.model.User;

public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String login, String password) {
        return userRepository.findByLogin(login)
                .filter(user -> user.getPasswordHash().equals(DigestUtils.sha256Hex(password)))
                .orElse(null);
    }
}