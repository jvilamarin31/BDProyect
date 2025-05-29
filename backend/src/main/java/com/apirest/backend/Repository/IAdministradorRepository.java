package com.apirest.backend.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apirest.backend.Model.AdministradorModel;

public interface IAdministradorRepository extends MongoRepository<AdministradorModel, ObjectId>{
    Optional<AdministradorModel> findByidAdministrador(ObjectId idAdministrador);
}
