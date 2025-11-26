package com.tec.usuarioac.mapper;

import com.tec.usuarioac.dto.UsuarioDto;
import com.tec.usuarioac.entity.Usuario;

public class UsuarioMapper {
	public static UsuarioDto mapToUsuarioDto(Usuario usuario) {
		if (usuario == null) {
			return null;
		} else {
			return new UsuarioDto(
					usuario.getId(),
					usuario.getCorreo(),
					usuario.getContrasena()
					);
		}
	}
	
	public static Usuario mapToUsuario(UsuarioDto usuarioDto) {
		if (usuarioDto == null) {
			return null;
		} else {
			return new Usuario(
					usuarioDto.getId(),
					usuarioDto.getCorreo(),
					usuarioDto.getContrasena()
					);
		}
	}

}
