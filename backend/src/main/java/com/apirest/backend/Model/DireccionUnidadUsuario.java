package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionUnidadUsuario {
    private String tipo;
    private String bloque;
    private String numero;
}
