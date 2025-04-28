package com.apirest.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Service.ISolicitudService;

@RestController
@RequestMapping("/api/solicitud")
public class SolicitudController {

    @Autowired
    ISolicitudService solicitudService;

    @PostMapping("/registrar")
    public ResponseEntity<SolicitudModel> crearSolicitud(@RequestBody SolicitudModel solicitud) {
        return new ResponseEntity<SolicitudModel>(solicitudService.crearSolicitud(solicitud), HttpStatus.CREATED);
    }
    
}
