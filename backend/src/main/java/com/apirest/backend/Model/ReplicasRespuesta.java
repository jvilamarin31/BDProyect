package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ReplicasRespuesta {
    @Id
    private ObjectId usuarioId;
    private String justificacionReaperturaUsuario;
    private String comentarioAdmin;
}
