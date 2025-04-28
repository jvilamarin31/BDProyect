package com.apirest.backend.Service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(usuarioExistente.isPresent()){
            if(usuarioExistente.get().getTipo()==TipoUsuario.usuario||usuarioExistente.get().getTipo()==TipoUsuario.anonimo){
                solicitud.setFechaHoraCreacion(Instant.now());
                solicitud.setEstado(EstadoSolicitud.radicada);
                if(solicitud.getEvidencias() != null){
                    for(EvidenciasSolicitud evidencias : solicitud.getEvidencias()){
                        evidencias.setFechaHora(Instant.now());
                    }
                }
                return solicitudRepository.save(solicitud);
            } else{
                throw new InvalidUserRoleException("Un administrador no puede crear una solicitud.");
            }
        } else{
            throw new ResourceNotFoundException("El usuario no existe.");
        }
    }
}
