package com.tec.usuarioac.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Proveedor")
public class Proveedor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "folioInterno", nullable = false)
	private String folioInterno;
	
	@Column(name = "folioFiscal", nullable = false)
	private String folioFiscal;
	
	@Column(name = "nombre", nullable = false)
	private String nombre;
	
	@Column(name = "rfc", nullable = false)
	private String rfc;
	
	@Column(name = "telefono", nullable = false)
	private String telefono;
	
	@Column(name = "direccion", nullable = false)
	private String direccion;
	
	@Column(name = "fechaRegistro", nullable = false)
	private LocalDateTime fechaRegistro;
	
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	
	/*id Empleado que lo dió de alta*/
	@ManyToOne
	@JoinColumn(name = "idEmpleado", nullable = false)
	private Empleado empleado;

}
