package com.apirest.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
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

            // SolicitudModel solicitud = solicitudExiste.get();

        // if (usuario.getId() != solicitud.getUsuario().getUsuarioId()){
        //     throw new InvalidUserRoleException("El usuario ingresado no corresponde al usuario que creo la solicitud.");
        // }
}
