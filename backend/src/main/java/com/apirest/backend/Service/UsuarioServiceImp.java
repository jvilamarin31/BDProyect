package com.apirest.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Exception.EmailAlreadyExistsException;
import com.apirest.backend.Exception.InvalidUserConfigurationException;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.TipoUsuario;
import com.apirest.backend.Repository.IUsuarioRepository;

@Service
public class UsuarioServiceImp implements IUsuarioService{

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Override
    public UsuarioModel crearUsuario(UsuarioModel usuario){
        Optional<UsuarioModel> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if(usuarioExistente.isPresent()) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }
        if (usuario.getTipo()==TipoUsuario.administrador){
            if ((usuario.getRol() != null)||(usuario.getDireccionUnidad()!= null)){
                throw new InvalidUserConfigurationException("Un administrador no puede tener rol ni dirección de unidad.");
            }
        } else if (usuario.getTipo()==TipoUsuario.usuario){
            if ((usuario.getRol() == null)||(usuario.getDireccionUnidad() == null)){
                throw new InvalidUserConfigurationException("Un usuario registrado debe tener rol y dirección de unidad.");
            }
        } 

        return usuarioRepository.save(usuario);   
    }
    

    
}
