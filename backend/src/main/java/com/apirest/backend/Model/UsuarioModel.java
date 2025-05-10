package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.apirest.backend.Model.ENUM.RolUsuario;
import com.apirest.backend.Model.ENUM.TipoUsuario;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private TipoUsuario tipo;
    private RolUsuario rol;
    private String nombreCompleto;
    private String tipoDocumento;
    private long numeroDocumento;
    private String email;
    private String contraseña;
    private long telefono;
    private DireccionUnidadUsuario direccionUnidad;
    
}
