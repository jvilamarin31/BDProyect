package com.apirest.backend.Service;

import java.time.Instant;
import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apirest.backend.Exception.InvalidReplicaConfigurationException;
import com.apirest.backend.Exception.InvalidRespuestaConfigurationException;
import com.apirest.backend.Exception.InvalidSolicitudConfigurationException;
import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Model.ReplicasRespuesta;
import com.apirest.backend.Model.RespuestaModel;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.EstadoAdministradores;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;
import com.apirest.backend.Model.ENUM.TipoUsuario;
import com.apirest.backend.Repository.IAdministradorRepository;
import com.apirest.backend.Repository.IRespuestaRepository;
import com.apirest.backend.Repository.ISolicitudRepository;
import com.apirest.backend.Repository.IUsuarioRepository;

@Service
public class RespuestaServiceImp implements IRespuestaService{
    @Autowired
    IRespuestaRepository respuestaRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    ISolicitudRepository solicitudRepository;

    @Autowired
    ISolicitudService solicitudService;

    @Autowired
    IAdministradorRepository administradorRepository;

    @Autowired
    IAdministradorService administradoresRepository;

    @Override
    @Transactional
    public RespuestaModel crearRespuesta(RespuestaModel respuesta){
        //Validación usuario admin
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(respuesta.getUsuarioId());
        if (!usuarioExiste.isPresent()){
            throw new ResourceNotFoundException("el usuario no existe.");
        }
        UsuarioModel administrador = usuarioExiste.get(); 
        if (administrador.getTipo() != TipoUsuario.administrador){
            throw new InvalidUserRoleException("Solo un administrador puede generar una respuesta.");
        }
        administradoresRepository.actualizarEstadosAdministradores();
        Optional<AdministradorModel> periodoAdministradorExiste = administradorRepository.findByidAdministrador(administrador.getId());
        if (!periodoAdministradorExiste.isPresent()) {
            throw new ResourceNotFoundException("El administrador no tiene periodo. Así que no se puede validar su estado. ");
        }
        AdministradorModel estadoAdministrador = periodoAdministradorExiste.get();
        if (estadoAdministrador.getEstado()!=EstadoAdministradores.activo) {
            throw new InvalidRespuestaConfigurationException("Solo un administrador en estado 'activo' puede responder una solicitud. ");
        }

        //Validación solicitud
        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(respuesta.getSolicitudId());
        if (!solicitudExiste.isPresent()){
            throw new ResourceNotFoundException("La solicitud no existe.");
        }
        SolicitudModel solicitud = solicitudExiste.get();
        if (solicitud.getUsuario().getNombreCompleto().equals("Usuario Anónimo")){
            throw new InvalidUserRoleException("Un administrador no puede responder a una solicitud de un usuario anónimo.");
        }


        //Validación del propio documento respuesta
        if ((respuesta.getCalificacionUsuario() != null) || (!respuesta.getReplicas().isEmpty() && respuesta.getReplicas() != null)){
            throw new InvalidSolicitudConfigurationException("Un administrador no puede calificar ni hacer replicas");
        }
        if (respuesta.getComentario().isBlank()) {
            throw new InvalidRespuestaConfigurationException("El comentario por parte del administrador es obligatorio, no se puede enviar vacio. ");
        }
        Optional<RespuestaModel> respuestaExiste = respuestaRepository.findBySolicitudId(respuesta.getSolicitudId());
        if (respuestaExiste.isPresent()) {
            throw new InvalidSolicitudConfigurationException("No se puede crear la respuesta, ya que esta solicitud ya fue respondida. ");
        }
        
        
        solicitud.setEstado(EstadoSolicitud.resuelta);
        solicitud.setFechaUltimaActualizacion(Instant.now());
        solicitudRepository.save(solicitud);
        
        return respuestaRepository.save(respuesta);
    }

    @Override
    @Transactional
    public RespuestaModel crearReplica(ObjectId idRespuesta, ReplicasRespuesta replica) {
        solicitudService.actualizarEstadoSolicitudesResueltas(); 

        Optional<RespuestaModel> respuestaExiste = respuestaRepository.findById(idRespuesta);
        if (!respuestaExiste.isPresent()) {
            throw new ResourceNotFoundException("La respuesta no existe.");
        }
        
        RespuestaModel respuestaActualizada = respuestaExiste.get();

        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(replica.getUsuarioId());
        if (!usuarioExiste.isPresent()) {
            throw new ResourceNotFoundException("El usuario no existe.");
        }

        UsuarioModel usuario = usuarioExiste.get();
        if (usuario.getTipo() != TipoUsuario.usuario) {
            throw new InvalidUserRoleException("Solo un usuario puede hacer una replica.");
        }

        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(respuestaActualizada.getSolicitudId());
        SolicitudModel solicitud = solicitudExiste.get();

        if ( !solicitud.getUsuario().getUsuarioId().equals(replica.getUsuarioId()) ) {
            throw new InvalidUserRoleException("Solo el usuario que hizo la solicitud puede hacer una replica.");
        }

        //Validaciones de los atributos enviados en la replica
        if (replica.getJustificacionReaperturaUsuario().isBlank()) {
            throw new InvalidReplicaConfigurationException("No se puede enviar la justificacion de reapertura vacio. ");
        }
        if (replica.getComentarioAdmin() != null) {
            throw new InvalidUserRoleException("Un usuario no puede hacer un comentario de administrador.");
        }

        if (respuestaActualizada.getReplicas() != null && !respuestaActualizada.getReplicas().isEmpty()) {
            ReplicasRespuesta ultimaReplica = respuestaActualizada.getReplicas().get(respuestaActualizada.getReplicas().size() - 1);
            if (ultimaReplica.getComentarioAdmin() == null || ultimaReplica.getComentarioAdmin().isEmpty()) {
                throw new InvalidUserRoleException("El usuario no puede hacer una replica si el administrador no ha respondido.");
            }
        }
        
        //validación de solicitud
        if (solicitud.getEstado() == EstadoSolicitud.cerrada) {
            throw new InvalidUserRoleException("No se puede hacer una replica a una solicitud que un administrador cerro de manera definitiva.");
        }
        


        respuestaActualizada.getReplicas().add(replica);
        solicitud.setEstado(EstadoSolicitud.reabierta);
        solicitudRepository.save(solicitud);
        return respuestaRepository.save(respuestaActualizada);

    }

    @Override
    @Transactional
    public RespuestaModel responderReplica(ObjectId idRespuesta, ReplicasRespuesta replica){
        Optional<RespuestaModel> respuestaExiste = respuestaRepository.findById(idRespuesta);
        if (!respuestaExiste.isPresent()) {
            throw new ResourceNotFoundException("El id: " + idRespuesta + " no corresponde a ninguna respuesta existente.");
        }
        RespuestaModel respuesta = respuestaExiste.get();

        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(respuesta.getSolicitudId());
        if (!solicitudExiste.isPresent()) {
            throw new ResourceNotFoundException("El id: " + respuesta.getSolicitudId() + " no corresponde a ninguna solicitud existente.");
        }

        SolicitudModel solicitud = solicitudExiste.get();

        if (replica.getComentarioAdmin() == null || replica.getComentarioAdmin().isEmpty()) {
            throw new InvalidUserRoleException("El comentario de administrador no puede estar vacio.");
        }
        
        if (replica.getUsuarioId() == null) {
            throw new InvalidUserRoleException("El id del usuario no puede estar vacio");
        }

        if (respuesta.getReplicas() == null || respuesta.getReplicas().isEmpty()) {
            throw new InvalidUserRoleException("No hay replicas para responder.");
        }
        
        ReplicasRespuesta ultimaReplica = respuesta.getReplicas().get(respuesta.getReplicas().size() - 1);
        if (ultimaReplica.getComentarioAdmin() != null && !ultimaReplica.getComentarioAdmin().isEmpty()) {
            throw new InvalidUserRoleException("No se puede responder a una replica que ya tiene un comentario de administrador.");
        }

        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(replica.getUsuarioId());
        if (!usuarioExiste.isPresent()) {
            throw new ResourceNotFoundException("El usuario no existe.");
        }
        // UsuarioModel usuario = usuarioExiste.get();
        // if (usuario.getTipo() != TipoUsuario.administrador) {
        //     throw new InvalidUserRoleException("Solo un administrador puede responder a una replica.");
        // } Este metodo es por si cambiamos de idea y queremos que cualquier admin pueda responder las replicas

        if (!respuesta.getUsuarioId().equals(replica.getUsuarioId())) {
            throw new InvalidUserRoleException("Solo el mismo administrador que creo la respuesta puede responder las replicas");
        }
        administradoresRepository.actualizarEstadosAdministradores();
        Optional<AdministradorModel> periodoAdministradorExiste = administradorRepository.findByidAdministrador(replica.getUsuarioId());
        if (!periodoAdministradorExiste.isPresent()) {
            throw new ResourceNotFoundException("El administrador no tiene periodo. Así que no se puede validar su estado.");
        }
        AdministradorModel estadoAdministrador = periodoAdministradorExiste.get();
        if (estadoAdministrador.getEstado()!=EstadoAdministradores.activo) {
            throw new InvalidRespuestaConfigurationException("Solo un administrador en estado 'activo' puede responder una replica. ");
        }

        if (replica.getEstado() != EstadoSolicitud.resuelta && replica.getEstado() != EstadoSolicitud.cerrada){
            throw new InvalidReplicaConfigurationException("El administrador solo puede cambiar el estado a 'resuelta' y 'cerrada' cuando responde una replica. ");
        }
        solicitud.setEstado(replica.getEstado());
        solicitud.setFechaUltimaActualizacion(Instant.now());
        solicitudRepository.save(solicitud);

        return respuestaRepository.save(respuesta);
    }

    @Override
    @Transactional
    public RespuestaModel calificarRespuesta (ObjectId idRespuesta, ObjectId idUsuario,Integer calificacion) {
        //Validacion de respuesta
        Optional<RespuestaModel> respuestaExiste = respuestaRepository.findById(idRespuesta);
        if (!respuestaExiste.isPresent()) {
            throw new ResourceNotFoundException("La respuesta con id: " + idRespuesta + "no existe. ");
        }
        RespuestaModel respuesta = respuestaExiste.get();

        //Validación de solicitud
        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(respuesta.getSolicitudId());

        if (!solicitudExiste.isPresent()) {
            throw new ResourceNotFoundException("La solicitud con id: " + respuesta.getSolicitudId() + "no existe. ");
        }
        SolicitudModel solicitud = solicitudExiste.get();
        
        if (solicitud.getEstado() != EstadoSolicitud.cerrada) {
            throw new InvalidRespuestaConfigurationException("Solo se puede calificar una respuesta si el estado de la solicitud se encuentra 'cerrada'");
        }

        //Validación de usuario
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(idUsuario);
        if (!usuarioExiste.isPresent()) {
            throw new ResourceNotFoundException("El usuario con id: " + idUsuario + "no existe. ");
        }
        UsuarioModel usuario = usuarioExiste.get();
        if (!solicitud.getUsuario().getUsuarioId().equals(usuario.getId())) {
            throw new InvalidUserRoleException("El usuario que va a calificar la respuesta debe ser el mismo que creo la solicitud. ");
        }
        if (respuesta.getCalificacionUsuario()!=null) {
            throw new InvalidRespuestaConfigurationException("El usuario solo puede calificar la respuesta una sola vez. ");
        }

        respuesta.setCalificacionUsuario(calificacion);
        return respuestaRepository.save(respuesta);
    }

    
         
}
