package com.tec.usuarioac.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para servir archivos de imagen
 * Permite acceder a las imágenes de perfil de empleados y clientes
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/uploads")
public class ImageController {
	
    @Value("${app.upload.dir.empleados:/uploads/empleados}")
    private String uploadDirEmpleados;
    
	@Value("${app.upload.dir.clientes:/uploads/clientes}")
	private String uploadDirClientes;

    /**
     * GET /uploads/empleados/{filename}
     * Sirve la imagen de perfil del empleado
     */
    @GetMapping("/empleados/{filename:.+}")
    public ResponseEntity<Resource> getImagenEmpleado(@PathVariable String filename) {
        try {
            // Construir ruta del archivo
            Path filePath = Paths.get(uploadDirEmpleados).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Verificar que el archivo existe
            if (resource.exists()) {
                // Determinar tipo de contenido
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Retornar la imagen
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /uploads/clientes/{filename}
     * Sirve la imagen de perfil del cliente
     */
    @GetMapping("/clientes/{filename:.+}")
    public ResponseEntity<Resource> getImagenCliente(@PathVariable String filename) {
        try {
            // Construir ruta del archivo
            Path filePath = Paths.get(uploadDirClientes).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            // Verificar que el archivo existe
            if (resource.exists()) {
                // Determinar tipo de contenido
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                
                // Retornar la imagen
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}