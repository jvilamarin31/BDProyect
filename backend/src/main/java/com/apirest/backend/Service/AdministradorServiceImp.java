package com.apirest.backend.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apirest.backend.Exception.InvalidPeriodoAdministradoresConfigurationException;
import com.apirest.backend.Exception.InvalidUserRoleException;
import com.apirest.backend.Exception.ResourceNotFoundException;
import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Model.PeriodosAdministradores;
import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Model.ENUM.EstadoAdministradores;
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
    @Transactional
    public AdministradorModel crearAdministradores(AdministradorModel administradores){

        //Validaciones del admin 
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findById(administradores.getIdAdministrador());
        if (!usuarioExiste.isPresent()) {
            throw new ResourceNotFoundException("El usuario con id: "+administradores.getIdAdministrador()+ " no existe. ");
        }
        UsuarioModel administrador = usuarioExiste.get();
        if (administrador.getTipo() != TipoUsuario.administrador) {
            throw new InvalidUserRoleException("Elusuario ingresado no es un administrador. ");
        }

        Optional<AdministradorModel> periodoAdministradorExiste = administradoresRepository.findByidAdministrador(administradores.getIdAdministrador());
        if (periodoAdministradorExiste.isPresent()) {
            throw new InvalidPeriodoAdministradoresConfigurationException("Ya existe un periodo administrador con ese usuario administrador. ");
        }
        
        PeriodosAdministradores ultimoPeriodo = administradores.getPeriodos().get(administradores.getPeriodos().size() - 1);
        Instant ahora = Instant.now();
        
        if ((!ahora.isBefore(ultimoPeriodo.getFechaInicio())) && (!ahora.isAfter(ultimoPeriodo.getFechaFin()))) {
            administradores.setEstado(EstadoAdministradores.activo);
        } else {
            administradores.setEstado(EstadoAdministradores.inactivo);
        }

        return administradoresRepository.save(administradores);
        
    }

    @Override 
    @Transactional
    public AdministradorModel modificarPeriodoAdministrador(ObjectId idAdministradorPeriodo, PeriodosAdministradores periodo) {
        Optional<AdministradorModel> AdministradorPeriodoExiste = administradoresRepository.findById(idAdministradorPeriodo);
        if (!AdministradorPeriodoExiste.isPresent()) {
            throw new ResourceNotFoundException("El id: " + idAdministradorPeriodo + " no corresponde a ningun periodo administrador. ");
        }

        AdministradorModel administradorActualizar = AdministradorPeriodoExiste.get();

        if (periodo.getFechaFin().isBefore(periodo.getFechaInicio())) {
            throw new InvalidPeriodoAdministradoresConfigurationException("La fecha fin no puede ser despues de la fecha inicio. ");
        }
        if (administradorActualizar.getPeriodos() != null && !administradorActualizar.getPeriodos().isEmpty()) {
            PeriodosAdministradores ultimoPeriodo = administradorActualizar.getPeriodos().get(administradorActualizar.getPeriodos().size() - 1);
            if (periodo.getFechaInicio().isBefore(ultimoPeriodo.getFechaFin())) {
                throw new InvalidPeriodoAdministradoresConfigurationException("La fecha de  incio del nuevo periodo debe ser despues de la fecha fin del anterior periodo. ");
            }
        }

        administradorActualizar.getPeriodos().add(periodo);
        actualizarEstadosAdministradores();
        return administradoresRepository.save(administradorActualizar);
        
    }

    @Override
    @Transactional
    public void actualizarEstadosAdministradores() {
        List<AdministradorModel> administradores = administradoresRepository.findAll();
        Instant ahora = Instant.now();

        for (AdministradorModel admin : administradores) {
            PeriodosAdministradores ultimoPeriodo = admin.getPeriodos().get(admin.getPeriodos().size() - 1);
            if ((!ahora.isBefore(ultimoPeriodo.getFechaInicio())) && (!ahora.isAfter(ultimoPeriodo.getFechaFin()))) {
                admin.setEstado(EstadoAdministradores.activo);
            } else {
                admin.setEstado(EstadoAdministradores.inactivo);
            }
        }
        administradoresRepository.saveAll(administradores);
    }

    @Override
    @Transactional
    public List<AdministradorModel> listarPeridosDeAdministradores() {
        actualizarEstadosAdministradores();
        List<AdministradorModel> periodos =administradoresRepository.findAll();
        return periodos;
    }
            
}
