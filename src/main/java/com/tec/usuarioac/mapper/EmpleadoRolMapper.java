package com.tec.usuarioac.mapper;

import com.tec.usuarioac.dto.EmpleadoRolDto;
import com.tec.usuarioac.entity.EmpleadoRol;

public class EmpleadoRolMapper {
	
	public static EmpleadoRolDto mapToEmpleadoRolDto(EmpleadoRol empleadoRol) {
		if (empleadoRol == null) {
			return null;
		} else {
			return new EmpleadoRolDto(
					empleadoRol.getId(),
					RolMapper.mapToRolDto(empleadoRol.getRol()),
					EmpleadoMapper.mapToEmpleadoDto(empleadoRol.getEmpleado())
					);
		}
	}
	
	public static EmpleadoRol mapToEmpleadoRol(EmpleadoRolDto empleadoRolDto) {
		if (empleadoRolDto == null) {
			return null;
		} else {
			return new EmpleadoRol(
					empleadoRolDto.getId(),
					RolMapper.mapToRol(empleadoRolDto.getRol()),
					EmpleadoMapper.mapToEmpleado(empleadoRolDto.getEmpleado())
					);
		}
	}

}
