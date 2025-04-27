package com.apirest.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.AdministradoresModel;
import com.apirest.backend.Service.IAdministradoresService;

@RestController
@RequestMapping("/api/administradores")
public class AdministradoresController {

    @Autowired
    IAdministradoresService administradoresService;
    
    @RequestMapping("/registrar")
    public ResponseEntity<AdministradoresModel> crearAdministradores(@RequestBody AdministradoresModel administradores) {
        return new ResponseEntity<AdministradoresModel>(administradoresService.crearAdministradores(administradores), HttpStatus.CREATED);
    }
    
}
