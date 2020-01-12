package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class ParkingSpaceStatus extends CommonEntity implements Serializable {

    public static final String FREE = "free";
    public static final String USED = "used";

    private String id;
    private String name;
    private String color;

    public ParkingSpaceStatus(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
