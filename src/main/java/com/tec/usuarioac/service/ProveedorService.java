package com.tec.usuarioac.service;

import java.util.List;

import com.tec.usuarioac.dto.ProveedorDto;

public interface ProveedorService {

    ProveedorDto nuevoProveedor(ProveedorDto proveedorDto);
    ProveedorDto actualizarProveedor(Long id, ProveedorDto proveedorDto);
    ProveedorDto buscarProveedor(Long id);
    void eliminarProveedor(Long id);
    List<ProveedorDto> listarTodosProveedores();
    List<ProveedorDto> listarProveedoresActivos();
    
}