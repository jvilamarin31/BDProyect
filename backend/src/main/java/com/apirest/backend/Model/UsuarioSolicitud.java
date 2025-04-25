package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UsuarioSolicitud {
    @Id
    private ObjectId usuarioId;
    private String nombreCompleto;
}
