package com.apirest.backend.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apirest.backend.Exception.InvalidSolicitudConfigurationException;
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
            throw new InvalidSolicitudConfigurationException("No se pueden agregar evidencias a una solicitud que este en estado 'cerrada'.");
        } else if (solicitud.getEstado() == EstadoSolicitud.resuelta) {
            throw new InvalidSolicitudConfigurationException("No se pueden agregar evidencias a una solicitud que este en estado 'resuelta'.");
        }
        evidencia.setFechaHora(Instant.now());
        solicitud.getEvidencias().add(evidencia);
        //solicitud.setFechaUltimaActualizacion(Instant.now()); el rpofe dijjo que cuando este atributo solo sería para cuadno se cambie el atributo estado.
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

    @Override
    @Transactional
    public List<SolicitudModel> actualizarEstadoSolicitudesResueltas() {
        // Buscar solicitudes con estado "resuelta"
        List<SolicitudModel> solicitudesResueltas = solicitudRepository.findByEstado(EstadoSolicitud.resuelta);
        List<SolicitudModel> solicitudesActualizadas = new ArrayList<>();
        Instant ahora = Instant.now();
        
        for (SolicitudModel solicitud : solicitudesResueltas) {
            // Sumamos 5 días a la fechaUltimaActualizacion de la solicitud
            Instant fechaLimite = solicitud.getFechaUltimaActualizacion().plus(5, ChronoUnit.DAYS);
            // Si la fecha actual ya sobrepasa el límite, cambiamos el estado a "cerrada"
            if (ahora.isAfter(fechaLimite)) {
                solicitud.setEstado(EstadoSolicitud.cerrada);
                solicitud.setFechaUltimaActualizacion(ahora);
                solicitudRepository.save(solicitud);
                solicitudesActualizadas.add(solicitud);
            }
        }
        return solicitudesActualizadas;
    }

    @Override
    @Transactional
    public SolicitudModel estadoEnProceso(ObjectId idSolicitud, ObjectId idAdministrador) {
        //Validaciones de la solicitud morrrr
        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(idSolicitud);
        if (!solicitudExiste.isPresent()) {
            throw new ResourceNotFoundException("Solicitud con id: "+ idSolicitud + " no encontrada. ");
        }
        SolicitudModel solicitud = solicitudExiste.get();
        if (solicitud.getEstado() != EstadoSolicitud.radicada && solicitud.getEstado() != EstadoSolicitud.reabierta) {
            throw new InvalidSolicitudConfigurationException("Solo se puede poner el estado de solicitud en proceso cuando el estado se encuentra en 'radicada' o 'reabierta'. ");
        }

        //Validaciones de usuario
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(idAdministrador);
        if (!usuarioExiste.isPresent()) {
            throw new ResourceNotFoundException("Usuario con id: "+ idAdministrador + " no existe. ");
        }
        UsuarioModel administrador = usuarioExiste.get();
        if (administrador.getTipo() != TipoUsuario.administrador) {
            throw new InvalidUserRoleException("Solo un administrador podra cambiar el estado a 'enProceso'. ");
        }

        solicitud.setEstado(EstadoSolicitud.enProceso);
        return solicitudRepository.save(solicitud);
        
    } 
}
