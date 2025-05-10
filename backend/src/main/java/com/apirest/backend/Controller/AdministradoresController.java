package com.apirest.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.AdministradorModel;
import com.apirest.backend.Service.IAdministradorService;

@RestController
@RequestMapping("/api/administrador")
public class AdministradoresController {

    @Autowired
    IAdministradorService administradoresService;
    
    @RequestMapping("/registrar")
    public ResponseEntity<AdministradorModel> crearAdministradores(@RequestBody AdministradorModel administradores) {
        return new ResponseEntity<AdministradorModel>(administradoresService.crearAdministradores(administradores), HttpStatus.CREATED);
    }
    
}
