package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class SeriesBooking implements Serializable {

    private String id;
    private User user;
    private boolean active;
    private LocalTime time;
    private int weekday;

    public SeriesBooking(String id, User user, boolean active, LocalTime time, int weekday) {
        this.id = id;
        this.user = user;
        this.active = active;
        this.time = time;
        this.weekday = weekday;
    }

}
