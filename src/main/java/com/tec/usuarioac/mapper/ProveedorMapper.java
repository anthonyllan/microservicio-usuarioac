package com.tec.usuarioac.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.tec.usuarioac.dto.ProveedorDto;
import com.tec.usuarioac.entity.Proveedor;

public class ProveedorMapper {
	
	public static ProveedorDto mapToProveedorDto(Proveedor proveedor) {
		if (proveedor == null) {
			return null;
		} else {
			return new ProveedorDto(
					proveedor.getId(),
					proveedor.getFolioInterno(),
					proveedor.getFolioFiscal(),
					proveedor.getNombre(),
					proveedor.getRfc(),
					proveedor.getTelefono(),
					proveedor.getDireccion(),
					proveedor.getFechaRegistro(),
					proveedor.getActivo(),
					/*En Impl del objeto empleado extraemos solo el "id"*/
					EmpleadoMapper.mapToEmpleadoDto(proveedor.getEmpleado())
					);
		}
	}
	
	public static Proveedor mapToProveedor(ProveedorDto proveedorDto) {
		if (proveedorDto == null) {
			return null;
		} 
		
		
		LocalDateTime fechaRegistro = proveedorDto.getFechaRegistro();
        if (fechaRegistro == null) {
            fechaRegistro  = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        } 
        
			return new Proveedor(
					proveedorDto.getId(),
					proveedorDto.getFolioInterno(),
					proveedorDto.getFolioFiscal(),
					proveedorDto.getNombre(),
					proveedorDto.getRfc(),
					proveedorDto.getTelefono(),
					proveedorDto.getDireccion(),
					fechaRegistro,
					proveedorDto.getActivo(),
					
					/*En Impl del objeto empleado extraemos solo el "id"*/
					EmpleadoMapper.mapToEmpleado(proveedorDto.getEmpleado())
					);
		
	}

}
