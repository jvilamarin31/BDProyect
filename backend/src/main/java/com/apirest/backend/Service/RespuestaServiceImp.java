package com.apirest.backend.Service;

import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
import com.apirest.backend.Model.ReplicasRespuesta;
import com.apirest.backend.Model.RespuestaModel;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.TipoUsuario;
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

    @Override
    public RespuestaModel crearRespuesta(RespuestaModel respuesta){
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(respuesta.getUsuarioId());
        Optional<SolicitudModel> solicitudExiste = solicitudRepository.findById(respuesta.getSolicitudId());

        if (!usuarioExiste.isPresent()){
            throw new ResourceNotFoundException("el usuario no existe.");
        }
        UsuarioModel usuario = usuarioExiste.get(); 
        if (usuario.getTipo() != TipoUsuario.administrador){
            throw new InvalidUserRoleException("Solo un administrador puede generar una respuesta.");
        }

        if (!solicitudExiste.isPresent()){
            throw new ResourceNotFoundException("La solicitud no existe.");
        }


        if ((respuesta.getCalificacionUsuario() != null) || (!respuesta.getReplicas().isEmpty() && respuesta.getReplicas() != null)){
            throw new InvalidUserRoleException("Un administrador no puede calificar ni hacer replicas");

        }
        return respuestaRepository.save(respuesta);
    }

    @Override
    public RespuestaModel crearReplica(ObjectId id, ReplicasRespuesta replica) {
        Optional<RespuestaModel> respuestaExiste = respuestaRepository.findById(id);
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

        if (replica.getComentarioAdmin() != null) {
            throw new InvalidUserRoleException("Un usuario no puede hacer un comentario de administrador.");
        }


        respuestaActualizada.getReplicas().add(replica);
    
        return respuestaRepository.save(respuestaActualizada);

    }
         
}
