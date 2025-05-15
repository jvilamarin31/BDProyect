package com.apirest.backend.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
import com.apirest.backend.Model.EvidenciasSolicitud;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;
import com.apirest.backend.Model.ENUM.TipoUsuario;
import com.apirest.backend.Repository.ISolicitudRepository;
import com.apirest.backend.Repository.IUsuarioRepository;

@Service
public class SolicitudServiceImp implements ISolicitudService{
    
    @Autowired
    ISolicitudRepository solicitudRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Override
    public SolicitudModel crearSolicitud(SolicitudModel solicitud){
        Optional<UsuarioModel> usuarioExistente = usuarioRepository.findById(solicitud.getUsuario().getUsuarioId());
        if (!usuarioExistente.isPresent()){
            throw new ResourceNotFoundException("El usuario no existe.");
        }
        UsuarioModel usuario = usuarioExistente.get();

        if (usuario.getTipo() == TipoUsuario.administrador){
            throw new InvalidUserRoleException("Un administrador no puede crear una solicitud.");
        }

        solicitud.setFechaHoraCreacion(Instant.now());
        solicitud.setEstado(EstadoSolicitud.radicada);

        if (usuario.getTipo() == TipoUsuario.anonimo){

            solicitud.getUsuario().setNombreCompleto(usuario.getNombreCompleto());
        }

        if (solicitud.getEvidencias() != null && !solicitud.getEvidencias().isEmpty()) {
            for (EvidenciasSolicitud evidencia : solicitud.getEvidencias()) {
                evidencia.setFechaHora(Instant.now());
            }
        }
        return solicitudRepository.save(solicitud);

    }
    @Override
    @Transactional
    public SolicitudModel agregarEvidencia(ObjectId idSolicitud, EvidenciasSolicitud evidencia) {
        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(idSolicitud);
        if (!solicitudExiste.isPresent()){
            throw new ResourceNotFoundException("La solicitud no existe.");
        }
        SolicitudModel solicitud = solicitudExiste.get();
        if (solicitud.getEstado() == EstadoSolicitud.cerrada) {
            throw new InvalidUserRoleException("No se pueden agregar evidencias a una solicitud que este en estado 'cerrada'.");
        } else if (solicitud.getEstado() == EstadoSolicitud.resuelta) {
            throw new InvalidUserRoleException("No se pueden agregar evidencias a una solicitud que este en estado 'resuelta'.");
        }
        evidencia.setFechaHora(Instant.now());
        solicitud.getEvidencias().add(evidencia);
        solicitud.setFechaUltimaActualizacion(Instant.now());
        return solicitudRepository.save(solicitud);
        
    }

    @Override
    public List<SolicitudModel> listarTodasSolicitudes() {
        List<SolicitudModel> solicitudes = solicitudRepository.findAll();
        return solicitudes;
    }

    @Override
    public List<SolicitudModel> listarSolicitudesPorEstado(EstadoSolicitud estado){ 
        return solicitudRepository.findByEstado(estado);
    }
}
