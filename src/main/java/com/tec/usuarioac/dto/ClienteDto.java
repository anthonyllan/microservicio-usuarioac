package com.tec.usuarioac.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
	
	private Long id;
	private UsuarioDto usuario;
	private String nombre;
	private String apellidos;
	private String telefono;
	private LocalDateTime fechaRegistro;
	private String imagen;
	
}
