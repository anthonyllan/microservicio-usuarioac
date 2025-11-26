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

import com.tec.usuarioac.dto.ClienteDto;
import com.tec.usuarioac.service.ClienteService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerClientePorId(@PathVariable Long id) {
        ClienteDto clienteDto = clienteService.obtenerClientePorId(id);
        return new ResponseEntity<>(clienteDto, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes() {
        List<ClienteDto> clientes = clienteService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
    
    // ==================== NUEVOS ENDPOINTS ====================
    
    /**
     * PUT /api/clientes/{id}
     * Actualizar perfil del cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(
            @PathVariable Long id, 
            @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteActualizado = clienteService.actualizarCliente(id, clienteDto);
        return ResponseEntity.ok(clienteActualizado);
    }
    
    /**
     * POST /api/clientes/{id}/imagen
     * Subir imagen de perfil
     */
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteDto> subirImagenPerfil(
            @PathVariable Long id,
            @RequestParam("imagen") MultipartFile imagen) throws IOException {
        ClienteDto clienteActualizado = clienteService.subirImagenPerfil(id, imagen);
        return ResponseEntity.ok(clienteActualizado);
    }
    
    /**
     * DELETE /api/clientes/{id}/imagen
     * Eliminar imagen de perfil
     */
    @DeleteMapping("/{id}/imagen")
    public ResponseEntity<ClienteDto> eliminarImagenPerfil(@PathVariable Long id) {
        ClienteDto clienteActualizado = clienteService.eliminarImagenPerfil(id);
        return ResponseEntity.ok(clienteActualizado);
    }
}