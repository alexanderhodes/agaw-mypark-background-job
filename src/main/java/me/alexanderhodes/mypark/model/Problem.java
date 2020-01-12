package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Problem implements Serializable {

    private String id;
    private LocalDateTime date;
    private String reason;
    private ParkingSpace parkingSpace;
    private User user;

    public Problem(String id, LocalDateTime date, String reason, ParkingSpace parkingSpace, User user) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.parkingSpace = parkingSpace;
        this.user = user;
    }
}
