package com.tec.usuarioac.service;

import java.util.List;

import com.tec.usuarioac.dto.RolDto;


public interface RolService {
    
    /* Guardar nuevo rol */
    RolDto guardarRol(RolDto rolDto);
    
    /* Obtener rol por id */
    RolDto obtenerRolPorId(Long id);
    
    /* Obtener rol por nombre */
    RolDto obtenerRolPorNombre(String nombre);
    
    /* Listar todos los roles */
    List<RolDto> listarRoles();
}