package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"username"})
public class User extends CommonEntity implements Serializable {

    private String id;
    private String name;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private ParkingSpace parkingSpace;

    public User(String id, String name, String password, String username, String firstName, String lastName,
                boolean enabled, ParkingSpace parkingSpace) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.parkingSpace = parkingSpace;
    }

    public User toJson() {
        return new User(this.id, this.name, null, this.username, this.firstName, this.lastName, this.enabled,
                this.parkingSpace);
    }
}
