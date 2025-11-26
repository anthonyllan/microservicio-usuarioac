package com.tec.usuarioac.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tec.usuarioac.dto.EmpleadoDto;

public interface EmpleadoService {
    
    /* Registrar nuevo empleado */
    EmpleadoDto registrarEmpleado(EmpleadoDto empleadoDto);
    
    /* Obtener empleado por id */
    EmpleadoDto obtenerEmpleadoPorId(Long id);
    
    /* Listar todos los empleados */
    List<EmpleadoDto> listarEmpleados();
    
    // ========== NUEVOS MÉTODOS PARA PERFIL ==========
    
    /* Actualizar datos del empleado */
    EmpleadoDto actualizarEmpleado(Long id, EmpleadoDto empleadoDto);
    
    /* Actualizar imagen de perfil */
    EmpleadoDto actualizarImagenPerfil(Long id, MultipartFile imagen) throws IOException;
    
    /* Eliminar imagen de perfil */
    EmpleadoDto eliminarImagenPerfil(Long id);
    
    /* Eliminar empleado */
    void eliminarEmpleado(Long id);
}