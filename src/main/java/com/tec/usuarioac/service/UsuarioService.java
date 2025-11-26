package com.tec.usuarioac.service;

import com.tec.usuarioac.dto.LoginRequestDto;
import com.tec.usuarioac.dto.LoginResponseDto;
import com.tec.usuarioac.dto.UsuarioDto;

public interface UsuarioService {
    UsuarioDto guardarUsuario(UsuarioDto usuarioDto);
    LoginResponseDto autenticarUsuario(LoginRequestDto loginRequestDto);
    boolean existeUsuarioPorCorreo(String correo);
    UsuarioDto obtenerUsuarioPorId(Long id);
}