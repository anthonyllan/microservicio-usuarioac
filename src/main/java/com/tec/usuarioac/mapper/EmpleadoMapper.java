package com.tec.usuarioac.mapper;

import com.tec.usuarioac.dto.EmpleadoDto;
import com.tec.usuarioac.entity.Empleado;

public class EmpleadoMapper {
	
	public static EmpleadoDto mapToEmpleadoDto(Empleado empleado) {
		if (empleado == null) {
			return null;
		} else {
			return new EmpleadoDto(
					empleado.getId(),
					UsuarioMapper.mapToUsuarioDto(empleado.getUsuario()),
					empleado.getNombre(),
					empleado.getApellidos(),
					empleado.getTelefono(),
					empleado.getImagen()
					);
		}
	}
	
	public static Empleado mapToEmpleado(EmpleadoDto empleadoDto) {
		if (empleadoDto == null) {
			return null;
		} else {
			return new Empleado(
					empleadoDto.getId(),
					UsuarioMapper.mapToUsuario(empleadoDto.getUsuario()),
					empleadoDto.getNombre(),
					empleadoDto.getApellidos(),
					empleadoDto.getTelefono(),
					empleadoDto.getImagen()	
					);
		}
	}

}
