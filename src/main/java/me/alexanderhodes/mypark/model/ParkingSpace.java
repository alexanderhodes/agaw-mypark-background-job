package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "number"})
public class ParkingSpace extends CommonEntity implements Serializable {

    private String id;
    private String number;
    private ParkingSpaceStatus parkingSpaceStatus;

    public ParkingSpace(String number, ParkingSpaceStatus parkingSpaceStatus) {
        this.number = number;
        this.parkingSpaceStatus = parkingSpaceStatus;
    }
}
