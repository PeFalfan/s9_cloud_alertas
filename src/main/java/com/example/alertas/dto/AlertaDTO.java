package com.example.alertas.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlertaDTO {
    private String mensaje;
    private String tipoAnomalia;
    private Long pacienteId;
}
