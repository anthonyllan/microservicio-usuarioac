package com.tec.usuarioac.service;

import com.tec.usuarioac.entity.Cliente;
import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.Usuario;
import com.tec.usuarioac.repository.ClienteRepository;
import com.tec.usuarioac.repository.EmpleadoRepository;
import com.tec.usuarioac.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(username);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario usuario = usuarioOpt.get();
        
        // Obtener autoridades (roles) basadas en si es cliente o empleado
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        // Verificar si es cliente
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuarioId(usuario.getId());
        if (clienteOpt.isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }
        
        // Verificar si es empleado
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuarioId(usuario.getId());
        if (empleadoOpt.isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLEADO"));
        }
        
        // Si no tiene ningún rol asignado, lanzar excepción
        if (authorities.isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin perfil asignado: " + username);
        }
        
        // Crear UserDetails con los datos del usuario
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}