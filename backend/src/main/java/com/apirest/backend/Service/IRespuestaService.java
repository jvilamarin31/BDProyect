package com.apirest.backend.Service;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.ReplicasRespuesta;
import com.apirest.backend.Model.RespuestaModel;

public interface IRespuestaService {
    public RespuestaModel crearRespuesta(RespuestaModel respuesta);
    public RespuestaModel crearReplica(ObjectId id, ReplicasRespuesta replica);
    public RespuestaModel responderReplica(ObjectId idRespuesta, ReplicasRespuesta replica);
}
