package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Token implements Serializable {

    public static final String PASSWORD_RESET = "password_reset";
    public static final String REGISTRATION = "registration";

    private String id;
    private User user;
    private LocalDateTime validUntil;
    private String category;

    public Token(String id, User user, LocalDateTime validUntil, String category) {
        this.id = id;
        this.user = user;
        this.validUntil = validUntil;
        this.category = category;
    }
}
