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
        Optional<UsuarioModel> usuarioEmailExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioEmailExistente.isPresent()) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }
        //Validacion de que no se envian datos que son requeridos, vacios
        if (usuario.getNombreCompleto().isBlank() || usuario.getTipoDocumento().isBlank() || usuario.getNumeroDocumento() == 0 || usuario.getEmail().isBlank() || usuario.getTelefono() == 0) {
            throw new InvalidUserConfigurationException("No se pueden enviar vacios los datos requeridos de la bd");
        }
        
        if (usuario.getTipo()==TipoUsuario.administrador){
            if ((usuario.getRol() != null)||(usuario.getDireccionUnidad()!= null)){
                throw new InvalidUserConfigurationException("Un administrador no puede tener rol ni dirección de unidad.");
            }
        } else if (usuario.getTipo()==TipoUsuario.usuario){
            if ((usuario.getRol() == null)||(usuario.getDireccionUnidad() == null)){
                throw new InvalidUserConfigurationException("Un usuario registrado debe tener rol y dirección de unidad.");
            }
            Optional<UsuarioModel> usuarioUbicacionExiste = usuarioRepository.findByDireccionUnidad(usuario.getDireccionUnidad());
            if (usuarioUbicacionExiste.isPresent()) {
                throw new InvalidUserConfigurationException("La dirección ingresada ya esta registrada por otro usuario. ");
            }
        } else if (usuario.getTipo()==TipoUsuario.anonimo){
            throw new InvalidUserConfigurationException("Un usuario no puede crear un usuario anónimo, para eso podra usar el usuario anonimo ya creado por defecto.");
        }//Validación para anonimo
        if (usuario.getNombreCompleto()=="Usuario Anónimo" || usuario.getNombreCompleto()=="Usuario Anonimo") {
            throw new InvalidUserConfigurationException("El nombre completo no puede ser 'Usuario Anónimo', ese nombre ya esta reservado para el usuario anonimo por defecto.");  
        }//Validación para anonimo
         

        return usuarioRepository.save(usuario);   
    }
    

    
}
