package com.apirest.backend.Controller;


import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.EvidenciasSolicitud;
import com.apirest.backend.Model.SolicitudModel;
import com.apirest.backend.Model.ENUM.EstadoSolicitud;
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

    @PutMapping("/agregarEvidencia/{id}")
    public ResponseEntity<SolicitudModel> agregarEvidencia(@PathVariable("id") ObjectId idSolicitud, @RequestBody EvidenciasSolicitud evidencia) {
        return new ResponseEntity<SolicitudModel>(solicitudService.agregarEvidencia(idSolicitud, evidencia), HttpStatus.OK);
    }

    @PutMapping("/estadoEnProceso/{idSolicitud}/{idAdministrador}")//crearPostman
    public ResponseEntity<SolicitudModel> estadoEnProceso(@PathVariable("idSolicitud") ObjectId idSolicitud,@PathVariable("idAdministrador") ObjectId idAdministrador) {
        return new ResponseEntity<SolicitudModel>(solicitudService.estadoEnProceso(idSolicitud, idAdministrador) , HttpStatus.OK);
    }

    @GetMapping("/listarSolicitudes")
    public ResponseEntity<List<SolicitudModel>> listarTodasSolicitudes() {
        return new ResponseEntity<List<SolicitudModel>> (solicitudService.listarTodasSolicitudes(),HttpStatus.OK);
    }

    @GetMapping("/listarSolicitudesPorEstado")
    public ResponseEntity<List<SolicitudModel>> listarSolicitudesPorEstado(@RequestBody EstadoSolicitud estado) {
        return new ResponseEntity<List<SolicitudModel>> (solicitudService.listarSolicitudesPorEstado(estado),HttpStatus.OK);
    }
    
    @PutMapping("/cambiarEstadoParaUnAnonimo/{idSolicitud}/{idAdministrador}")
    public ResponseEntity<SolicitudModel> cambiarEstadoSolicitudParaUnAnonimo (@PathVariable("idSolicitud") ObjectId idSolicitud,@PathVariable("idAdministrador") ObjectId idAdministrador,@RequestBody EstadoSolicitud estado) {
        return new ResponseEntity<SolicitudModel> (solicitudService.cambiarEstadoSolicitudParaUnAnonimo(idSolicitud, idAdministrador, estado),HttpStatus.OK);
    }
}
