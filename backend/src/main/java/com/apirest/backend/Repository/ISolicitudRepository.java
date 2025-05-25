package com.apirest.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.UsuarioSolicitud;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;

public interface ISolicitudRepository extends MongoRepository<SolicitudModel, ObjectId>{
    List<SolicitudModel>findByEstado(EstadoSolicitud estado);
    Optional<SolicitudModel>findByUsuarioAndDescripcionDetallada(UsuarioSolicitud usuario,String descripcionDetallada);
}
