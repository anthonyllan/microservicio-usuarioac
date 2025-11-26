package com.tec.usuarioac.serviceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tec.usuarioac.dto.ProveedorDto;
import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.Proveedor;
import com.tec.usuarioac.exception.ResourceNotFoundException;
import com.tec.usuarioac.mapper.ProveedorMapper;
import com.tec.usuarioac.repository.EmpleadoRepository;
import com.tec.usuarioac.repository.ProveedorRepository;
import com.tec.usuarioac.service.ProveedorService;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public ProveedorDto nuevoProveedor(ProveedorDto proveedorDto) {
        // Validar que el empleado existe
        if (proveedorDto.getEmpleado() == null || proveedorDto.getEmpleado().getId() == null) {
            throw new IllegalArgumentException("Debe especificar un empleado válido");
        }
        
        Empleado empleado = empleadoRepository.findById(proveedorDto.getEmpleado().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + proveedorDto.getEmpleado().getId()));
        
        // Crear entidad Proveedor
        Proveedor proveedor = new Proveedor();
        proveedor.setFolioInterno(proveedorDto.getFolioInterno());
        proveedor.setFolioFiscal(proveedorDto.getFolioFiscal());
        proveedor.setNombre(proveedorDto.getNombre());
        proveedor.setRfc(proveedorDto.getRfc());
        proveedor.setTelefono(proveedorDto.getTelefono());
        proveedor.setDireccion(proveedorDto.getDireccion());
        proveedor.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        proveedor.setActivo(proveedorDto.getActivo() != null ? proveedorDto.getActivo() : true);
        proveedor.setEmpleado(empleado);
        
        // Guardar
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        
        return ProveedorMapper.mapToProveedorDto(proveedorGuardado);
    }

    @Override
    public ProveedorDto actualizarProveedor(Long id, ProveedorDto proveedorDto) {
        // Buscar proveedor existente
        Proveedor proveedorExistente = proveedorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));
        
        // Actualizar campos
        if (proveedorDto.getFolioInterno() != null) {
            proveedorExistente.setFolioInterno(proveedorDto.getFolioInterno());
        }
        if (proveedorDto.getFolioFiscal() != null) {
            proveedorExistente.setFolioFiscal(proveedorDto.getFolioFiscal());
        }
        if (proveedorDto.getNombre() != null) {
            proveedorExistente.setNombre(proveedorDto.getNombre());
        }
        if (proveedorDto.getRfc() != null) {
            proveedorExistente.setRfc(proveedorDto.getRfc());
        }
        if (proveedorDto.getTelefono() != null) {
            proveedorExistente.setTelefono(proveedorDto.getTelefono());
        }
        if (proveedorDto.getDireccion() != null) {
            proveedorExistente.setDireccion(proveedorDto.getDireccion());
        }
        if (proveedorDto.getActivo() != null) {
            proveedorExistente.setActivo(proveedorDto.getActivo());
        }
        
        // Actualizar empleado si se proporciona
        if (proveedorDto.getEmpleado() != null && proveedorDto.getEmpleado().getId() != null) {
            Empleado empleado = empleadoRepository.findById(proveedorDto.getEmpleado().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + proveedorDto.getEmpleado().getId()));
            proveedorExistente.setEmpleado(empleado);
        }
        
        // Guardar cambios
        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);
        
        return ProveedorMapper.mapToProveedorDto(proveedorActualizado);
    }

    @Override
    public ProveedorDto buscarProveedor(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));
        
        return ProveedorMapper.mapToProveedorDto(proveedor);
    }

    @Override
    public void eliminarProveedor(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));
        
        proveedorRepository.delete(proveedor);
    }

    @Override
    public List<ProveedorDto> listarTodosProveedores() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream()
            .map(ProveedorMapper::mapToProveedorDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProveedorDto> listarProveedoresActivos() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream()
            .filter(Proveedor::getActivo)
            .map(ProveedorMapper::mapToProveedorDto)
            .collect(Collectors.toList());
    }
}