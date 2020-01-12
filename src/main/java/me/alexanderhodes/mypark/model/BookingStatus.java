package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class BookingStatus implements Serializable {

    public static final String REQUEST = "requested";
    public static final String CONFIRMED = "confirmed";
    public static final String OCCUPIED = "occupied";
    public static final String DECLINED = "declined";

    private String id;
    private String name;
    private String color;

    public BookingStatus(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
