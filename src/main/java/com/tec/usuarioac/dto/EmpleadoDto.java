package com.tec.usuarioac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDto {

	private Long id;
	private UsuarioDto usuario;
	private String nombre;
	private String apellidos;
	private String telefono;
	private String imagen;
	
}
