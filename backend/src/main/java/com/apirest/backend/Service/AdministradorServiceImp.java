package com.apirest.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.TipoUsuario;
import com.apirest.backend.Repository.IAdministradorRepository;
import com.apirest.backend.Repository.IUsuarioRepository;

@Service
public class AdministradorServiceImp implements IAdministradorService{

    @Autowired
    IAdministradorRepository administradoresRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    

    @Override
    public AdministradorModel crearAdministradores(AdministradorModel administradores){
        Optional<UsuarioModel> usuarioOpcional = usuarioRepository.findById(administradores.getIdAdministrador());
        
        if((usuarioOpcional.isPresent())){
            UsuarioModel usuario = usuarioOpcional.get();
            if(usuario.getTipo()==TipoUsuario.administrador){
                return administradoresRepository.save(administradores);
            }else{
                throw new InvalidUserRoleException("El usuario no es un administrador.");
            }
        }else{
            throw new ResourceNotFoundException("el usuario no existe.");
        }
    }

            
}
