package com.robsonkades.springcamelexample;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DFeEvent {

    private String nsu;
    private String link;
    private LocalDateTime currentDate;
}
