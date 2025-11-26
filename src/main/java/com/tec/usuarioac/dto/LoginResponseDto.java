package com.tec.usuarioac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String correo;
    private String token;
    private String tipo; // "CLIENTE" o "EMPLEADO"
    private String rol; // "CAJERO", "GERENTE", "ADMIN" (solo para empleados, null para clientes)
    private String nombre;
    private String apellidos;
}

