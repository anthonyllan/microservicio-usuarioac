package com.tec.usuarioac.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tec.usuarioac.dto.RolDto;
import com.tec.usuarioac.entity.Rol;
import com.tec.usuarioac.exception.ResourceNotFoundException;
import com.tec.usuarioac.mapper.RolMapper;
import com.tec.usuarioac.repository.RolRepository;
import com.tec.usuarioac.service.RolService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public RolDto guardarRol(RolDto rolDto) {
        // Verificar si el rol ya existe por nombre
        if (rolDto.getNombre() != null && rolRepository.findByNombre(rolDto.getNombre()).isPresent()) {
            throw new ResourceNotFoundException("El rol ya existe con nombre: " + rolDto.getNombre());
        }
        
        Rol rol = RolMapper.mapToRol(rolDto);
        Rol nuevoRol = rolRepository.save(rol);
        return RolMapper.mapToRolDto(nuevoRol);
    }
    
    @Override
    public RolDto obtenerRolPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return RolMapper.mapToRolDto(rol);
    }
    
    @Override
    public RolDto obtenerRolPorNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con nombre: " + nombre));
        return RolMapper.mapToRolDto(rol);
    }
    
    @Override
    public List<RolDto> listarRoles() {
        List<Rol> roles = rolRepository.findAll();
        return roles.stream()
                .map(RolMapper::mapToRolDto)
                .collect(Collectors.toList());
    }
}