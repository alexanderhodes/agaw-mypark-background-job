package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Booking extends CommonEntity implements Serializable {

    private String id;
    private User user;
    private ParkingSpace parkingSpace;
    private LocalDateTime date;
    private BookingStatus bookingStatus;

    public Booking(User user, ParkingSpace parkingSpace, LocalDateTime date) {
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.date = date;
    }
}
