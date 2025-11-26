package com.tec.usuarioac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec.usuarioac.dto.RolDto;
import com.tec.usuarioac.service.RolService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;
    
    @PostMapping
    public ResponseEntity<RolDto> guardarRol(@RequestBody RolDto rolDto) {
        RolDto nuevoRol = rolService.guardarRol(rolDto);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RolDto> obtenerRolPorId(@PathVariable Long id) {
        RolDto rolDto = rolService.obtenerRolPorId(id);
        return new ResponseEntity<>(rolDto, HttpStatus.OK);
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolDto> obtenerRolPorNombre(@PathVariable String nombre) {
        RolDto rolDto = rolService.obtenerRolPorNombre(nombre);
        return new ResponseEntity<>(rolDto, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<RolDto>> listarRoles() {
        List<RolDto> roles = rolService.listarRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}