package com.apirest.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.RespuestaModel;
import com.apirest.backend.Service.IRespuestaService;

@RestController
@RequestMapping("/api/respuesta")
public class RespuestaController {
    @Autowired
    IRespuestaService respuestaService;

    @PostMapping("/registrar")
    public ResponseEntity<RespuestaModel> crearRespueesta(@RequestBody RespuestaModel respuesta){
        return new ResponseEntity<RespuestaModel>(respuestaService.crearRespuesta(respuesta),HttpStatus.CREATED);
    }
}
