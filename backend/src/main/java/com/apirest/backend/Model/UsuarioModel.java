package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.apirest.backend.ENUM.RolUsuario;
import com.apirest.backend.ENUM.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel {
    @Id
    private ObjectId id;
    private TipoUsuario tipo;
    private RolUsuario rol;
    private String nombreCompleto;
    private String tipoDocumento;
    private int numeroDocumento;
    private String email;
    private String contraseña;
    private int telefono;;
    private DireccionUnidadUsuario direccionUnidad;
    
}
