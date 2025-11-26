	package com.tec.usuarioac.serviceImpl;
	
	import java.util.List;
	import java.util.stream.Collectors;
	
	import org.springframework.stereotype.Service;
	
	import com.tec.usuarioac.dto.EmpleadoDto;
	import com.tec.usuarioac.dto.EmpleadoRolDto;
	import com.tec.usuarioac.dto.RolDto;
	import com.tec.usuarioac.entity.Empleado;
	import com.tec.usuarioac.entity.EmpleadoRol;
	import com.tec.usuarioac.entity.Rol;
	import com.tec.usuarioac.exception.ResourceNotFoundException;
	import com.tec.usuarioac.mapper.EmpleadoMapper;
	import com.tec.usuarioac.mapper.EmpleadoRolMapper;
	import com.tec.usuarioac.mapper.RolMapper;
	import com.tec.usuarioac.repository.EmpleadoRepository;
	import com.tec.usuarioac.repository.EmpleadoRolRepository;
	import com.tec.usuarioac.repository.RolRepository;
	import com.tec.usuarioac.service.EmpleadoRolService;
	
	import lombok.RequiredArgsConstructor;
	
	@Service
	@RequiredArgsConstructor
	public class EmpleadoRolServiceImpl implements EmpleadoRolService {
	
	    private final EmpleadoRolRepository empleadoRolRepository;
	    private final EmpleadoRepository empleadoRepository;
	    private final RolRepository rolRepository;
	
	    @Override
	    public EmpleadoRolDto guardaRolEmpleado(EmpleadoRolDto empleadoRolDto) {
	        // Buscar entidades para asegurar que existen
	        Empleado empleado = empleadoRepository.findById(empleadoRolDto.getEmpleado().getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
	        
	        Rol rol = rolRepository.findById(empleadoRolDto.getRol().getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
	        
	        // Crear y guardar la relación
	        EmpleadoRol empleadoRol = new EmpleadoRol();
	        empleadoRol.setRol(rol);
	        empleadoRol.setEmpleado(empleado);
	        
	        EmpleadoRol saved = empleadoRolRepository.save(empleadoRol);
	        
	        return EmpleadoRolMapper.mapToEmpleadoRolDto(saved);
	    }
	    
	    @Override
	    public List<RolDto> obtenerRolesPorEmpleado(Long empleadoId) {
	        Empleado empleado = empleadoRepository.findById(empleadoId)
	                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
	        
	        List<EmpleadoRol> roles = empleadoRolRepository.findByEmpleado(empleado);
	        
	        return roles.stream()
	                .map(er -> RolMapper.mapToRolDto(er.getRol()))
	                .collect(Collectors.toList());
	    }
	    
	    @Override
	    public List<EmpleadoDto> obtenerEmpleadosPorRol(Long rolId) {
	        Rol rol = rolRepository.findById(rolId)
	                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
	        
	        List<EmpleadoRol> empleados = empleadoRolRepository.findByRol(rol);
	        
	        return empleados.stream()
	                .map(er -> EmpleadoMapper.mapToEmpleadoDto(er.getEmpleado()))
	                .collect(Collectors.toList());
	    }
	}