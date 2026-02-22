package pl.edu.wszib.library.core;

import pl.edu.wszib.library.model.User;

public interface IAuthenticationService {
    User authenticate(String login, String password);
}