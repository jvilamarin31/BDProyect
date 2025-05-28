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

import com.apirest.backend.Model.ReplicasRespuesta;
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

    @PutMapping("/registarReplica/{idRespuesta}")
    public ResponseEntity<RespuestaModel> crearReplica(@PathVariable("idRespuesta") ObjectId idRespuesta, @RequestBody ReplicasRespuesta replica){
        return new ResponseEntity<RespuestaModel> (respuestaService.crearReplica(idRespuesta, replica),HttpStatus.OK);
    }

    @PutMapping("/responderReplica/{idRespuesta}")
    public ResponseEntity<RespuestaModel> responderReplica(@PathVariable("idRespuesta") ObjectId idRespuesta, @RequestBody ReplicasRespuesta replica){
        return new ResponseEntity<RespuestaModel> (respuestaService.responderReplica(idRespuesta, replica),HttpStatus.OK);
    }

    @PutMapping("/calificarRespuesta/{idRespuesta}/{idUsuario}")
    public ResponseEntity<RespuestaModel> crearCalificacion(@PathVariable("idRespuesta") ObjectId idRespuesta,@PathVariable("idUsuario") ObjectId idUsuario,@RequestBody Integer calificacion) {
        return new ResponseEntity<RespuestaModel> (respuestaService.calificarRespuesta(idRespuesta, idUsuario, calificacion),HttpStatus.OK);
    }
}
