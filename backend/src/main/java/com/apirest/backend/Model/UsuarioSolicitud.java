package com.apirest.backend.Model;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

@Data
public class UsuarioSolicitud {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId usuarioId;
    private String nombreCompleto;
}
