package com.tec.usuarioac.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.tec.usuarioac.entity.Cliente;
import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.EmpleadoRol;
import com.tec.usuarioac.entity.Rol;
import com.tec.usuarioac.entity.Usuario;
import com.tec.usuarioac.repository.ClienteRepository;
import com.tec.usuarioac.repository.EmpleadoRepository;
import com.tec.usuarioac.repository.EmpleadoRolRepository;
import com.tec.usuarioac.repository.RolRepository;
import com.tec.usuarioac.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class SystemInitializer {

    @Bean
    @Transactional
    CommandLineRunner initSystem(
            UsuarioRepository usuarioRepository,
            EmpleadoRepository empleadoRepository,
            RolRepository rolRepository,
            EmpleadoRolRepository empleadoRolRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            System.out.println("\n===========================================");
            System.out.println("🚀 INICIANDO CONFIGURACIÓN DEL SISTEMA...");
            System.out.println("===========================================\n");
            
            // ==================== PASO 1: CREAR ROLES ====================
            Rol rolAdmin = null;
            if (rolRepository.findByNombre("r_admin").isEmpty()) {
                rolAdmin = new Rol();
                rolAdmin.setNombre("r_admin");
                rolAdmin = rolRepository.save(rolAdmin);
                System.out.println("✅ Rol ADMIN creado con ID: " + rolAdmin.getId());
            } else {
                rolAdmin = rolRepository.findByNombre("r_admin").get();
                System.out.println("ℹ️  Rol ADMIN ya existe con ID: " + rolAdmin.getId());
            }
            
            if (rolRepository.findByNombre("r_gerente").isEmpty()) {
                Rol rolGerente = new Rol();
                rolGerente.setNombre("r_gerente");
                rolRepository.save(rolGerente);
                System.out.println("✅ Rol GERENTE creado");
            } else {
                System.out.println("ℹ️  Rol GERENTE ya existe");
            }
            
            if (rolRepository.findByNombre("r_cajero").isEmpty()) {
                Rol rolCajero = new Rol();
                rolCajero.setNombre("r_cajero");
                rolRepository.save(rolCajero);
                System.out.println("✅ Rol CAJERO creado");
            } else {
                System.out.println("ℹ️  Rol CAJERO ya existe");
            }
            
            // ==================== PASO 2: CREAR USUARIO ADMIN ====================
            String adminEmail = "admin@gmail.com";
            String adminPassword = "admin123";
            
            Usuario adminUsuario = null;
            Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(adminEmail);
            
            if (usuarioExistente.isEmpty()) {
                adminUsuario = new Usuario();
                adminUsuario.setCorreo(adminEmail);
                adminUsuario.setContrasena(passwordEncoder.encode(adminPassword));
                adminUsuario = usuarioRepository.save(adminUsuario);
                System.out.println("✅ Usuario admin creado con ID: " + adminUsuario.getId());
            } else {
                adminUsuario = usuarioExistente.get();
                System.out.println("ℹ️  Usuario admin ya existe con ID: " + adminUsuario.getId());
            }
            
            // ==================== PASO 3: CREAR EMPLEADO ADMIN ====================
            Empleado adminEmpleado = null;
            Optional<Empleado> empleadoExistente = empleadoRepository.findByUsuarioId(adminUsuario.getId());
            
            if (empleadoExistente.isEmpty()) {
                adminEmpleado = new Empleado();
                adminEmpleado.setUsuario(adminUsuario);
                adminEmpleado.setNombre("Administrador");
                adminEmpleado.setApellidos("Sistema");
                adminEmpleado.setTelefono("0000000000");
                adminEmpleado = empleadoRepository.save(adminEmpleado);
                System.out.println("✅ Empleado admin creado con ID: " + adminEmpleado.getId());
            } else {
                adminEmpleado = empleadoExistente.get();
                System.out.println("ℹ️  Empleado admin ya existe con ID: " + adminEmpleado.getId());
            }
            
            // ==================== PASO 4: ASIGNAR ROL ADMIN ====================
            if (empleadoRolRepository.findByEmpleadoId(adminEmpleado.getId()).isEmpty()) {
                EmpleadoRol empleadoRol = new EmpleadoRol();
                empleadoRol.setEmpleado(adminEmpleado);
                empleadoRol.setRol(rolAdmin);
                empleadoRolRepository.save(empleadoRol);
                System.out.println("✅ Rol ADMIN asignado al empleado admin");
            } else {
                System.out.println("ℹ️  El empleado admin ya tiene roles asignados");
            }
            
            // ==================== PASO 5: CREAR CLIENTE DE MOSTRADOR ====================
            System.out.println("\n-------------------------------------------");
            System.out.println("📋 Verificando cliente de mostrador...");
            System.out.println("-------------------------------------------");
            
            try {
                String mostradorEmail = "mostrador@restaurante.local";
                Optional<Usuario> usuarioMostradorBusqueda = usuarioRepository.findByCorreo(mostradorEmail);
                
                Usuario usuarioMostrador;
                if (usuarioMostradorBusqueda.isEmpty()) {
                    // Crear usuario de mostrador
                    usuarioMostrador = new Usuario();
                    usuarioMostrador.setCorreo(mostradorEmail);
                    usuarioMostrador.setContrasena(passwordEncoder.encode("Mostrador123!"));
                    usuarioMostrador = usuarioRepository.save(usuarioMostrador);
                    System.out.println("✅ Usuario de mostrador creado con ID: " + usuarioMostrador.getId());
                } else {
                    usuarioMostrador = usuarioMostradorBusqueda.get();
                    System.out.println("ℹ️  Usuario de mostrador ya existe con ID: " + usuarioMostrador.getId());
                }
                
                // Verificar si ya existe un cliente asociado
                Optional<Cliente> clienteExistente = clienteRepository.findByUsuarioId(usuarioMostrador.getId());
                
                if (clienteExistente.isEmpty()) {
                    System.out.println("⚠️  Creando cliente de mostrador...");
                    
                    // ✅ CREAR CLIENTE CON TODOS LOS CAMPOS OBLIGATORIOS
                    Cliente clienteMostrador = new Cliente();
                    clienteMostrador.setUsuario(usuarioMostrador);
                    clienteMostrador.setNombre("PICKUP");
                    clienteMostrador.setApellidos("EN MOSTRADOR");
                    clienteMostrador.setTelefono("0000000000");
                    clienteMostrador.setFechaRegistro(LocalDateTime.now()); // ✅ AGREGADO
                    // Nota: imagen es nullable, no es necesario establecerlo
                    
                    Cliente clienteGuardado = clienteRepository.save(clienteMostrador);
                    
                    System.out.println("✅ Cliente de mostrador creado exitosamente:");
                    System.out.println("   - ID Cliente: " + clienteGuardado.getId());
                    System.out.println("   - Nombre: " + clienteGuardado.getNombre() + " " + clienteGuardado.getApellidos());
                    System.out.println("   - Usuario: " + clienteGuardado.getUsuario().getCorreo());
                    System.out.println("   - Fecha Registro: " + clienteGuardado.getFechaRegistro());
                    System.out.println("   - Propósito: Pedidos de mostrador (PICKUP_EN_MOSTRADOR)");
                } else {
                    Cliente cliente = clienteExistente.get();
                    System.out.println("✅ Cliente de mostrador ya existe:");
                    System.out.println("   - ID Cliente: " + cliente.getId());
                    System.out.println("   - Nombre: " + cliente.getNombre() + " " + cliente.getApellidos());
                    System.out.println("   - Usuario: " + cliente.getUsuario().getCorreo());
                }
                
            } catch (Exception e) {
                System.err.println("\n❌ ERROR AL CREAR CLIENTE DE MOSTRADOR:");
                System.err.println("   Tipo: " + e.getClass().getName());
                System.err.println("   Mensaje: " + e.getMessage());
                e.printStackTrace();
            }
            
            // ==================== RESUMEN FINAL ====================
            System.out.println("\n===========================================");
            System.out.println("✅ CONFIGURACIÓN INICIAL COMPLETADA");
            System.out.println("===========================================");
            
            System.out.println("\n📝 CREDENCIALES DEL ADMINISTRADOR:");
            System.out.println("   Usuario:    admin@gmail.com");
            System.out.println("   Contraseña: admin123");
            
            System.out.println("\n📝 CREDENCIALES DEL CLIENTE DE MOSTRADOR:");
            System.out.println("   Usuario:    mostrador@restaurante.local");
            System.out.println("   Contraseña: Mostrador123!");
            System.out.println("   ⚠️  NOTA: Este cliente es solo para uso interno del sistema.");
            
            System.out.println("\n📊 ESTADÍSTICAS DE LA BASE DE DATOS:");
            System.out.println("   Usuarios:       " + usuarioRepository.count());
            System.out.println("   Empleados:      " + empleadoRepository.count());
            System.out.println("   Clientes:       " + clienteRepository.count());
            System.out.println("   Roles:          " + rolRepository.count());
            System.out.println("   EmpleadosRoles: " + empleadoRolRepository.count());
            System.out.println("\n===========================================\n");
        };
    }
}