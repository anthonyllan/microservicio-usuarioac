package com.tec.usuarioac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter	
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoRolDto {
	
	private Long id;
	private RolDto rol;
	private EmpleadoDto empleado;
	
	
}
