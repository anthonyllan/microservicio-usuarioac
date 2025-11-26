package com.tec.usuarioac.service;

import java.util.List;

import com.tec.usuarioac.dto.EmpleadoDto;
import com.tec.usuarioac.dto.EmpleadoRolDto;
import com.tec.usuarioac.dto.RolDto;


public interface EmpleadoRolService {
    
    /* El admin asigna un rol al empleado */
    EmpleadoRolDto guardaRolEmpleado(EmpleadoRolDto empleadoRolDto);
    
    /* Obtener roles por empleado */
    List<RolDto> obtenerRolesPorEmpleado(Long empleadoId);
    
    /* Obtener empleados por rol */
    List<EmpleadoDto> obtenerEmpleadosPorRol(Long rolId);
}