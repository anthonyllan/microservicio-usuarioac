package com.tec.usuarioac.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.tec.usuarioac.dto.ClienteDto;
import com.tec.usuarioac.entity.Cliente;

public class ClienteMapper {
	
	public static ClienteDto mapToClienteDto(Cliente cliente) {
		if (cliente == null) {
			return null;
		} else {
			return new ClienteDto(
					cliente.getId(),
					UsuarioMapper.mapToUsuarioDto(cliente.getUsuario()),
					cliente.getNombre(),
					cliente.getApellidos(),
					cliente.getTelefono(),
					cliente.getFechaRegistro(),
					cliente.getImagen()
					);
		}
	}
	public static Cliente mapToCliente(ClienteDto clienteDto) {
		if (clienteDto == null) {
			return null;
		}
		
		LocalDateTime fechaRegistro = clienteDto.getFechaRegistro();
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        } return new Cliente(
        		clienteDto.getId(),
        		UsuarioMapper.mapToUsuario(clienteDto.getUsuario()),
        		clienteDto.getNombre(),
        		clienteDto.getApellidos(),
        		clienteDto.getTelefono(),
        		fechaRegistro,
        		clienteDto.getImagen()
        		);
	}

}
