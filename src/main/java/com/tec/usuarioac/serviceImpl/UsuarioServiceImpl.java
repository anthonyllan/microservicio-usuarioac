package com.tec.usuarioac.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tec.usuarioac.dto.LoginRequestDto;
import com.tec.usuarioac.dto.LoginResponseDto;
import com.tec.usuarioac.dto.UsuarioDto;
import com.tec.usuarioac.entity.Cliente;
import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.EmpleadoRol;
import com.tec.usuarioac.entity.Rol;
import com.tec.usuarioac.entity.Usuario;
import com.tec.usuarioac.exception.ResourceNotFoundException;
import com.tec.usuarioac.mapper.UsuarioMapper;
import com.tec.usuarioac.repository.ClienteRepository;
import com.tec.usuarioac.repository.EmpleadoRepository;
import com.tec.usuarioac.repository.EmpleadoRolRepository;
import com.tec.usuarioac.repository.UsuarioRepository;
import com.tec.usuarioac.security.JwtUtil;
import com.tec.usuarioac.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoRolRepository empleadoRolRepository; // ✅ NUEVO: Agregar repositorio de EmpleadoRol
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UsuarioDto guardarUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = UsuarioMapper.mapToUsuario(usuarioDto);
        
        // Encriptar contraseña si no está ya encriptada
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return UsuarioMapper.mapToUsuarioDto(usuarioGuardado);
    }

    @Override
    public LoginResponseDto autenticarUsuario(LoginRequestDto loginRequestDto) {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(loginRequestDto.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales incorrectas"));

        // Verificar contraseña
        if (!passwordEncoder.matches(loginRequestDto.getContrasena(), usuario.getContrasena())) {
            throw new ResourceNotFoundException("Credenciales incorrectas");
        }

        // Determinar si es cliente o empleado
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuarioId(usuario.getId());
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuarioId(usuario.getId());

        LoginResponseDto response = new LoginResponseDto();
        response.setCorreo(usuario.getCorreo());

        if (clienteOpt.isPresent()) {
            // Es un cliente
            Cliente cliente = clienteOpt.get();
            response.setId(cliente.getId());
            response.setTipo("CLIENTE");
            response.setNombre(cliente.getNombre());
            response.setApellidos(cliente.getApellidos());
            response.setRol(null); // Los clientes no tienen rol
            response.setToken(jwtUtil.generateToken(usuario.getCorreo(), "CLIENTE"));
        } else if (empleadoOpt.isPresent()) {
            // Es un empleado
            Empleado empleado = empleadoOpt.get();
            response.setId(empleado.getId());
            response.setNombre(empleado.getNombre());
            response.setApellidos(empleado.getApellidos());
            response.setTipo("EMPLEADO");
            
            // ✅ NUEVO: Obtener el rol específico del empleado
            String rol = obtenerRolEmpleado(empleado.getId());
            response.setRol(rol);
            
            response.setToken(jwtUtil.generateToken(usuario.getCorreo(), "EMPLEADO"));
        } else {
            throw new ResourceNotFoundException("Usuario sin perfil asignado");
        }

        return response;
    }

    /**
     * ✅ NUEVO: Obtener el rol específico del empleado desde la base de datos
     * @param empleadoId ID del empleado
     * @return Rol del empleado ("CAJERO", "GERENTE", "ADMIN") o null si no tiene rol
     */
    private String obtenerRolEmpleado(Long empleadoId) {
        try {
            // Buscar los roles del empleado en la tabla empleadorol
            List<EmpleadoRol> empleadoRoles = empleadoRolRepository.findByEmpleadoId(empleadoId);
            
            if (empleadoRoles == null || empleadoRoles.isEmpty()) {
                // Si no tiene roles asignados, retornar null
                return null;
            }
            
            // Obtener el primer rol (o el principal si hay múltiples)
            // En la mayoría de casos, un empleado tiene un solo rol
            EmpleadoRol empleadoRol = empleadoRoles.get(0);
            Rol rol = empleadoRol.getRol();
            
            if (rol == null || rol.getNombre() == null) {
                return null;
            }
            
            String nombreRol = rol.getNombre();
            
            // Mapear el nombre del rol de la BD a la forma esperada por el frontend
            switch (nombreRol) {
                case "r_cajero":
                    return "CAJERO";
                case "r_gerente":
                    return "GERENTE";
                case "r_admin":
                    return "ADMIN";
                default:
                    // Si el nombre del rol no coincide, retornar en mayúsculas
                    return nombreRol.toUpperCase().replace("R_", "");
            }
        } catch (Exception e) {
            // En caso de error, registrar y retornar null
            System.err.println("Error al obtener rol del empleado " + empleadoId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    @Override
    public UsuarioDto obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return UsuarioMapper.mapToUsuarioDto(usuario);
    }
}

