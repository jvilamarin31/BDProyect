package com.apirest.backend.Model;

import java.time.Instant;
import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.apirest.backend.Model.ENUM.ClasificacionSolicitud;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;
import com.apirest.backend.Model.ENUM.TipoSolicitud;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Solicitud")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudModel {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private TipoSolicitud tipo;
    private ClasificacionSolicitud clasificacion;
    private String descripcionDetallada;
    private UsuarioSolicitud usuario;
    private Instant fechaHoraCreacion;
    private EstadoSolicitud estado;
    private Instant fechaUltimaActualizacion;
    private ArrayList<EvidenciasSolicitud> evidencias = new ArrayList<>();
}