package me.alexanderhodes.mypark.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Absence implements Serializable {

    private String id;
    private User user;
    private LocalDate start;
    private LocalDate end;

    public Absence(User user, LocalDate start, LocalDate end) {
        this.user = user;
        this.start = start;
        this.end = end;
    }
}
