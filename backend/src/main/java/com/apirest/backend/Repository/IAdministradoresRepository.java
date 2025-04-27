package com.apirest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apirest.backend.Model.AdministradoresModel;

public interface IAdministradoresRepository extends MongoRepository<AdministradoresModel, ObjectId>{
    
}
