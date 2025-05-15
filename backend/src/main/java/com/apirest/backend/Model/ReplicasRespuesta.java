package com.apirest.backend.Model;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.ENUM.EstadoSolicitud;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

@Data
public class ReplicasRespuesta {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId usuarioId;
    private String justificacionReaperturaUsuario;
    private String comentarioAdmin;
    private EstadoSolicitud estado;
}
