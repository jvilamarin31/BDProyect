package com.apirest.backend.Controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Model.PeriodosAdministradores;
import com.apirest.backend.Service.IAdministradorService;

@RestController
@RequestMapping("/api/administrador")
public class AdministradoresController {

    @Autowired
    IAdministradorService administradoresService;
    
    @PostMapping("/registrar")
    public ResponseEntity<AdministradorModel> crearAdministradores(@RequestBody AdministradorModel administradores) {
        return new ResponseEntity<AdministradorModel>(administradoresService.crearAdministradores(administradores), HttpStatus.CREATED);
    }

    @PutMapping("/modificarAcuerdo/{idAdministradorPeriodo}")
    public ResponseEntity<AdministradorModel> modificarPeriodoAdministrador(@PathVariable("idAdministradorPeriodo") ObjectId idAdministradorPeriodo,@RequestBody PeriodosAdministradores periodo) {
        return new ResponseEntity<AdministradorModel>(administradoresService.modificarPeriodoAdministrador(idAdministradorPeriodo, periodo),HttpStatus.OK);
    }
    
}
