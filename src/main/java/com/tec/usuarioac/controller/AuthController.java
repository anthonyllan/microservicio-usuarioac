package com.tec.usuarioac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec.usuarioac.dto.LoginRequestDto;
import com.tec.usuarioac.dto.LoginResponseDto;
import com.tec.usuarioac.dto.RegistroClienteDto;
import com.tec.usuarioac.service.ClienteService;
import com.tec.usuarioac.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = usuarioService.autenticarUsuario(loginRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistroClienteDto registroClienteDto) {
        return new ResponseEntity<>(clienteService.registroNuevoCliente(registroClienteDto), HttpStatus.CREATED);
    }
}