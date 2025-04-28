package com.apirest.backend.Model;

import org.bson.types.ObjectId;


import lombok.Data;

@Data
public class UsuarioSolicitud {
    private ObjectId usuarioId;
    private String nombreCompleto;
}
