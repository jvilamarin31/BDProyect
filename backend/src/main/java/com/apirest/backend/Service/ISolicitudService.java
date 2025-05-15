package com.apirest.backend.Service;


import java.util.List;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.EvidenciasSolicitud;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;

public interface ISolicitudService {
    public SolicitudModel crearSolicitud(SolicitudModel solicitud);
    public SolicitudModel agregarEvidencia(ObjectId idSolicitud, EvidenciasSolicitud evidencia);
    public List<SolicitudModel> listarTodasSolicitudes(); //Falta testear
    public List<SolicitudModel> listarSolicitudesPorEstado(EstadoSolicitud estado); //Falta testear
}
