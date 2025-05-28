package com.apirest.backend.Service;


import java.util.List;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.EvidenciasSolicitud;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;

public interface ISolicitudService {
    public SolicitudModel crearSolicitud(SolicitudModel solicitud);
    public SolicitudModel agregarEvidencia(ObjectId idSolicitud, EvidenciasSolicitud evidencia);
    public void actualizarEstadoSolicitudesResueltas(); 
    public List<SolicitudModel> listarTodasSolicitudes(); 
    public List<SolicitudModel> listarSolicitudesPorEstado(EstadoSolicitud estado); 
    public SolicitudModel estadoEnProceso(ObjectId idSolicitud, ObjectId idAdministrador); 
    public SolicitudModel cambiarEstadoSolicitudParaUnAnonimo (ObjectId idSolicitud, ObjectId idAdministrador, EstadoSolicitud estado);
}
