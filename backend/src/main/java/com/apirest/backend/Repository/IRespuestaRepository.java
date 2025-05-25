package com.apirest.backend.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apirest.backend.Model.RespuestaModel;

public interface IRespuestaRepository extends MongoRepository<RespuestaModel, ObjectId>{
    Optional<RespuestaModel> findBySolicitudId(ObjectId solicitudId);
}
