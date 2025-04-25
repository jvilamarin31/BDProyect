package com.apirest.backend.Model;

import java.time.Instant;

import lombok.Data;

@Data
public class PeriodosAdministradores {
    private Instant fechaInicio;
    private Instant fechaFin;
}
