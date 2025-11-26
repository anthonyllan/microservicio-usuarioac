package com.tec.usuarioac.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tec.usuarioac.dto.ClienteDto;
import com.tec.usuarioac.dto.RegistroClienteDto;

public interface ClienteService {
    
    /* Cliente se registra */
    ClienteDto registroCliente(ClienteDto clienteDto);
    
    /* Registro desde DTO completo */
    ClienteDto registroNuevoCliente(RegistroClienteDto registroClienteDto);
    
    /* Obtener cliente por id */
    ClienteDto obtenerClientePorId(Long id);
    
    /* Listar todos los clientes */
    List<ClienteDto> listarClientes();
    
    /* NUEVOS MÉTODOS PARA PERFIL */
    
    /* Actualizar perfil del cliente */
    ClienteDto actualizarCliente(Long id, ClienteDto clienteDto);
    
    /* Subir imagen de perfil */
    ClienteDto subirImagenPerfil(Long id, MultipartFile imagen) throws IOException;
    
    /* Eliminar imagen de perfil */
    ClienteDto eliminarImagenPerfil(Long id);
}