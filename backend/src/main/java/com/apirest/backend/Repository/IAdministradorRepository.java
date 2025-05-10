package com.apirest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apirest.backend.Model.AdministradorModel;

public interface IAdministradorRepository extends MongoRepository<AdministradorModel, ObjectId>{
    
}
