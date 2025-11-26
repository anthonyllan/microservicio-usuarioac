package com.tec.usuarioac.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.EmpleadoRol;
import com.tec.usuarioac.entity.Rol;

@Repository
public interface EmpleadoRolRepository extends JpaRepository<EmpleadoRol, Long> {
    
    
    // Para EmpleadoRolServiceImpl
    List<EmpleadoRol> findByEmpleado(Empleado empleado);
    
    // Para EmpleadoRolServiceImpl
    List<EmpleadoRol> findByRol(Rol rol);
    
    /* Buscar roles por empleado */
    List<EmpleadoRol> findByEmpleadoId(Long empleadoId);
    
    /* Eliminar roles por empleado */
    void deleteByEmpleadoId(Long empleadoId);
    
    Optional<EmpleadoRol> findByEmpleadoIdAndRolId(Long empleadoId, Long rolId);
    
}