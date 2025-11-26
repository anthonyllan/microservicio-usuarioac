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
public class ProveedorDto {
	
	private Long id;
	private String folioInterno;
	private String folioFiscal;
	private String nombre;
	private String rfc;
	private String telefono;
	private String direccion;
	private LocalDateTime fechaRegistro;
	private Boolean activo;
	
	/*Empleado que lo dió de alta*/
	private EmpleadoDto empleado;

}
