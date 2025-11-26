package com.tec.usuarioac.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tec.usuarioac.dto.EmpleadoDto;
import com.tec.usuarioac.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    
    @PostMapping
    public ResponseEntity<EmpleadoDto> registrarEmpleado(@RequestBody EmpleadoDto empleadoDto) {
        EmpleadoDto nuevoEmpleado = empleadoService.registrarEmpleado(empleadoDto);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDto> obtenerEmpleadoPorId(@PathVariable Long id) {
        EmpleadoDto empleadoDto = empleadoService.obtenerEmpleadoPorId(id);
        return new ResponseEntity<>(empleadoDto, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<EmpleadoDto>> listarEmpleados() {
        List<EmpleadoDto> empleados = empleadoService.listarEmpleados();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    // ========== NUEVOS ENDPOINTS PARA PERFIL ==========
    
    /**
     * PUT /api/empleados/{id}
     * Actualizar datos del empleado (perfil)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDto> actualizarEmpleado(
            @PathVariable Long id, 
            @RequestBody EmpleadoDto empleadoDto) {
        EmpleadoDto empleadoActualizado = empleadoService.actualizarEmpleado(id, empleadoDto);
        return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
    }
    
    /**
     * POST /api/empleados/{id}/imagen
     * Subir o actualizar imagen de perfil
     */
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmpleadoDto> subirImagenPerfil(
            @PathVariable Long id,
            @RequestParam("imagen") MultipartFile imagen) throws IOException {
        EmpleadoDto empleadoActualizado = empleadoService.actualizarImagenPerfil(id, imagen);
        return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
    }
    
    /**
     * DELETE /api/empleados/{id}/imagen
     * Eliminar imagen de perfil
     */
    @DeleteMapping("/{id}/imagen")
    public ResponseEntity<EmpleadoDto> eliminarImagenPerfil(@PathVariable Long id) {
        EmpleadoDto empleadoActualizado = empleadoService.eliminarImagenPerfil(id);
        return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
    }
    
    /**
     * DELETE /api/empleados/{id}
     * Eliminar empleado (y sus roles en cascada)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.ok("Empleado eliminado exitosamente");
    }
    
    
}