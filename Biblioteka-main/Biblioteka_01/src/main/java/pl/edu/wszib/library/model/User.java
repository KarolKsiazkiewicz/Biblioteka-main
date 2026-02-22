package pl.edu.wszib.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String login;
    private String passwordHash; // Tu trzymamy hash, nie hasło!
    private Role role;
}