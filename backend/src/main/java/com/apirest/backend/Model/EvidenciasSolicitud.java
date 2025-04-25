package com.apirest.backend.Model;

import java.time.Instant;

import com.apirest.backend.Model.ENUM.TipoArchivoEvidencias;

import lombok.Data;

@Data
public class EvidenciasSolicitud {
    private TipoArchivoEvidencias tipoArchivo;
    private String descripcion;
    private Instant fechaHora;
}
