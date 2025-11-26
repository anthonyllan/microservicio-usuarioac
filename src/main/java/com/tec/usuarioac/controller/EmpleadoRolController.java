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

import com.tec.usuarioac.dto.EmpleadoDto;
import com.tec.usuarioac.dto.EmpleadoRolDto;
import com.tec.usuarioac.dto.RolDto;
import com.tec.usuarioac.service.EmpleadoRolService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/empleado-roles")
@RequiredArgsConstructor
public class EmpleadoRolController {

    private final EmpleadoRolService empleadoRolService;
    
    @PostMapping
    public ResponseEntity<EmpleadoRolDto> asignarRolEmpleado(@RequestBody EmpleadoRolDto empleadoRolDto) {
        EmpleadoRolDto nuevoEmpleadoRol = empleadoRolService.guardaRolEmpleado(empleadoRolDto);
        return new ResponseEntity<>(nuevoEmpleadoRol, HttpStatus.CREATED);
    }
    
    @GetMapping("/empleado/{id}/roles")
    public ResponseEntity<List<RolDto>> obtenerRolesPorEmpleado(@PathVariable Long id) {
        List<RolDto> roles = empleadoRolService.obtenerRolesPorEmpleado(id);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    
    @GetMapping("/rol/{id}/empleados")
    public ResponseEntity<List<EmpleadoDto>> obtenerEmpleadosPorRol(@PathVariable Long id) {
        List<EmpleadoDto> empleados = empleadoRolService.obtenerEmpleadosPorRol(id);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
}