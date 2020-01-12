package me.alexanderhodes.mypark.model.helper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.alexanderhodes.mypark.model.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"user", "admin"})
public class UserAdmin implements Serializable {

    private User user;
    private boolean admin;

    public UserAdmin(User user, boolean admin) {
        this.user = user.toJson();
        this.admin = admin;
    }
}
