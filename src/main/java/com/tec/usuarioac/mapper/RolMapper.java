package com.tec.usuarioac.mapper;

import com.tec.usuarioac.dto.RolDto;
import com.tec.usuarioac.entity.Rol;

public class RolMapper {

	public static RolDto mapToRolDto(Rol rol) {
		if (rol == null) {
			return null;
		} else {
			return new RolDto(
					rol.getId(),
					rol.getNombre()
					);
		}
	}
	public static Rol mapToRol(RolDto rolDto) {
		if (rolDto == null) {
			return null;
		} else {
			return new Rol(
					rolDto.getId(),
					rolDto.getNombre()
					);
		}
	}
	
}
