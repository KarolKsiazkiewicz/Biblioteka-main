package pl.edu.wszib.library.database;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.library.model.Role;
import pl.edu.wszib.library.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new User("admin", DigestUtils.sha256Hex("admin"), Role.ADMIN));
        users.add(new User("user", DigestUtils.sha256Hex("user"), Role.USER));
    }

    public Optional<User> findByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }
}