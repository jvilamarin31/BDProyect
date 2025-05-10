package com.apirest.backend.Service;


import org.bson.types.ObjectId;

import com.apirest.backend.Model.EvidenciasSolicitud;
import com.apirest.backend.Model.SolicitudModel;

public interface ISolicitudService {
    public SolicitudModel crearSolicitud(SolicitudModel solicitud);
    public SolicitudModel agregarEvidencia(ObjectId idSolicitud, EvidenciasSolicitud evidencia);
}
