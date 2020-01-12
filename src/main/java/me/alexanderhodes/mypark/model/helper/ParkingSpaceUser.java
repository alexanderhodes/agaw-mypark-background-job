package me.alexanderhodes.mypark.model.helper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.alexanderhodes.mypark.model.ParkingSpace;
import me.alexanderhodes.mypark.model.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {})
public class ParkingSpaceUser implements Serializable {

    private ParkingSpace parkingSpace;
    private User user;

    public ParkingSpaceUser(ParkingSpace parkingSpace, User user) {
        this.parkingSpace = parkingSpace;
        this.user = user != null ? user.toJson() : null;
    }
}
