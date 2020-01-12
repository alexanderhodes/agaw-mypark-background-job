package me.alexanderhodes.mypark.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Auth {

    private String token;
    private long expiration;
    private List<String> roles;
    private String username;

    public Auth(String token, long expiration, List<String> roles, String username) {
        this.token = token;
        this.expiration = expiration;
        this.roles = roles;
        this.username = username;
    }
}
