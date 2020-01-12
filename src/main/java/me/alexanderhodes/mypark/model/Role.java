package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Role implements Serializable {

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
