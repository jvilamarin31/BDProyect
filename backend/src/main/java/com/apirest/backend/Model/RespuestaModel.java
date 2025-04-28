package com.apirest.backend.Model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Respuesta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaModel {
    @Id
    private ObjectId id;
    private ObjectId solicitudId;
    private ObjectId usuarioId;
    private String oficioPDF;
    private String comentario;
    private Integer calificacionUsuario;
    private ArrayList<ReplicasRespuesta> replicas = new ArrayList<>();
    
}
