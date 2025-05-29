package com.apirest.backend.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Model.PeriodosAdministradores;

public interface IAdministradorService {
    public AdministradorModel crearAdministradores(AdministradorModel administradores);
    public AdministradorModel modificarPeriodoAdministrador(ObjectId idAdministradorPeriodo, PeriodosAdministradores periodo);
    public void actualizarEstadosAdministradores();
    public List<AdministradorModel> listarPeridosDeAdministradores();
}
