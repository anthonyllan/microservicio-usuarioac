package com.tec.usuarioac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec.usuarioac.dto.ProveedorDto;
import com.tec.usuarioac.service.ProveedorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * POST /api/proveedores
     * Crear un nuevo proveedor
     */
    @PostMapping
    public ResponseEntity<ProveedorDto> crearProveedor(@RequestBody ProveedorDto proveedorDto) {
        ProveedorDto nuevoProveedor = proveedorService.nuevoProveedor(proveedorDto);
        return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
    }

    /**
     * GET /api/proveedores/{id}
     * Buscar un proveedor por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> buscarProveedor(@PathVariable Long id) {
        ProveedorDto proveedor = proveedorService.buscarProveedor(id);
        return ResponseEntity.ok(proveedor);
    }

    /**
     * GET /api/proveedores
     * Listar todos los proveedores
     */
    @GetMapping
    public ResponseEntity<List<ProveedorDto>> listarTodosProveedores() {
        List<ProveedorDto> proveedores = proveedorService.listarTodosProveedores();
        return ResponseEntity.ok(proveedores);
    }

    /**
     * GET /api/proveedores/activos
     * Listar solo proveedores activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorDto>> listarProveedoresActivos() {
        List<ProveedorDto> proveedores = proveedorService.listarProveedoresActivos();
        return ResponseEntity.ok(proveedores);
    }

    /**
     * PUT /api/proveedores/{id}
     * Actualizar un proveedor existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> actualizarProveedor(
            @PathVariable Long id, 
            @RequestBody ProveedorDto proveedorDto) {
        ProveedorDto proveedorActualizado = proveedorService.actualizarProveedor(id, proveedorDto);
        return ResponseEntity.ok(proveedorActualizado);
    }

    /**
     * DELETE /api/proveedores/{id}
     * Eliminar un proveedor
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.ok("Proveedor eliminado exitosamente");
    }
}