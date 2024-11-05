package tn.esprit.spring.dto;

import lombok.Data;

@Data
public class PisteDTO {
    private Long numPiste;
    private String namePiste;
    private String color;
    private int length;
    private int slope;
}
