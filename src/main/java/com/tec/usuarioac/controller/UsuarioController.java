package com.tec.usuarioac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tec.usuarioac.dto.UsuarioDto;
import com.tec.usuarioac.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDto usuarioDto = usuarioService.obtenerUsuarioPorId(id);
        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }
}