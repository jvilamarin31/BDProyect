package com.apirest.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.UsuarioModel;
import com.apirest.backend.Service.IUsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    IUsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> crearUsuario(@RequestBody UsuarioModel usuario) {
        return new ResponseEntity<UsuarioModel>(usuarioService.crearUsuario(usuario), HttpStatus.CREATED);
    }
    
}
