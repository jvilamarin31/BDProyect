package com.apirest.backend.Model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class ReplicasRespuesta {
    private ObjectId usuarioId;
    private String justificacionReaperturaUsuario;
    private String comentarioAdmin;
}
