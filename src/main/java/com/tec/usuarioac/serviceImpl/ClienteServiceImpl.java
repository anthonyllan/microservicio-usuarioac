package com.tec.usuarioac.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tec.usuarioac.dto.ClienteDto;
import com.tec.usuarioac.dto.RegistroClienteDto;
import com.tec.usuarioac.dto.UsuarioDto;
import com.tec.usuarioac.entity.Cliente;
import com.tec.usuarioac.entity.Usuario;
import com.tec.usuarioac.exception.ResourceNotFoundException;
import com.tec.usuarioac.mapper.ClienteMapper;
import com.tec.usuarioac.repository.ClienteRepository;
import com.tec.usuarioac.repository.UsuarioRepository;
import com.tec.usuarioac.service.ClienteService;
import com.tec.usuarioac.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.upload.dir.clientes:/uploads/clientes}")
    private String uploadDir;

    @Override
    public ClienteDto registroCliente(ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.mapToCliente(clienteDto);
        
        // Establecer fecha de registro si no existe
        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        }
        
        Cliente nuevoCliente = clienteRepository.save(cliente);
        return ClienteMapper.mapToClienteDto(nuevoCliente);
    }

    @Override
    @Transactional
    public ClienteDto registroNuevoCliente(RegistroClienteDto registroClienteDto) {
        // Verificar si el correo ya existe
        if (usuarioService.existeUsuarioPorCorreo(registroClienteDto.getCorreo())) {
            throw new ResourceNotFoundException("El correo ya está registrado");
        }
        
        // Crear usuario
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setCorreo(registroClienteDto.getCorreo());
        usuarioDto.setContrasena(registroClienteDto.getContrasena());
        
        UsuarioDto usuarioGuardado = usuarioService.guardarUsuario(usuarioDto);
        
        // Crear cliente
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setUsuario(usuarioGuardado);
        clienteDto.setNombre(registroClienteDto.getNombre());
        clienteDto.setApellidos(registroClienteDto.getApellidos());
        clienteDto.setTelefono(registroClienteDto.getTelefono());
        clienteDto.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        
        return registroCliente(clienteDto);
    }

    @Override
    public ClienteDto obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return ClienteMapper.mapToClienteDto(cliente);
    }

    @Override
    public List<ClienteDto> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteMapper::mapToClienteDto)
                .collect(Collectors.toList());
    }
    
    // ==================== NUEVOS MÉTODOS ====================
    
    @Override
    @Transactional
    public ClienteDto actualizarCliente(Long id, ClienteDto clienteDto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        
        // Actualizar campos del cliente
        if (clienteDto.getNombre() != null) {
            clienteExistente.setNombre(clienteDto.getNombre());
        }
        if (clienteDto.getApellidos() != null) {
            clienteExistente.setApellidos(clienteDto.getApellidos());
        }
        if (clienteDto.getTelefono() != null) {
            clienteExistente.setTelefono(clienteDto.getTelefono());
        }
        
        // Actualizar usuario si se proporciona
        if (clienteDto.getUsuario() != null) {
            Usuario usuario = clienteExistente.getUsuario();
            
            if (clienteDto.getUsuario().getCorreo() != null) {
                // Verificar si el nuevo correo ya existe (excepto el actual)
                if (!usuario.getCorreo().equals(clienteDto.getUsuario().getCorreo()) && 
                    usuarioService.existeUsuarioPorCorreo(clienteDto.getUsuario().getCorreo())) {
                    throw new IllegalArgumentException("El correo ya está en uso");
                }
                usuario.setCorreo(clienteDto.getUsuario().getCorreo());
            }
            
            if (clienteDto.getUsuario().getContrasena() != null && !clienteDto.getUsuario().getContrasena().isEmpty()) {
                String contrasenaPlana = clienteDto.getUsuario().getContrasena();
                
                // Verificar si ya está encriptada (comienza con $2a$)
                if (contrasenaPlana.startsWith("$2a$")) {
                    System.out.println("ℹ️  [ClienteService] Contraseña ya encriptada, no se modifica");
                } else {
                    // Encriptar la contraseña
                    String contrasenaEncriptada = passwordEncoder.encode(contrasenaPlana);
                    usuario.setContrasena(contrasenaEncriptada);
                    System.out.println("🔐 [ClienteService] Contraseña encriptada correctamente");
                }
            }
            
            usuarioRepository.save(usuario);
        }
        
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return ClienteMapper.mapToClienteDto(clienteActualizado);
    }
    
    @Override
    @Transactional
    public ClienteDto subirImagenPerfil(Long id, MultipartFile imagen) throws IOException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        
        // Validar que el archivo no esté vacío
        if (imagen.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        // Validar tipo de contenido
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        
        // Crear directorio si no existe
        Path directorioPath = Paths.get(uploadDir);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath);
        }
        
        // Eliminar imagen anterior si existe
        if (cliente.getImagen() != null) {
            try {
                Path imagenAnterior = directorioPath.resolve(cliente.getImagen());
                Files.deleteIfExists(imagenAnterior);
            } catch (IOException e) {
                // Log error but continue
                System.err.println("No se pudo eliminar la imagen anterior: " + e.getMessage());
            }
        }
        
        // Generar nombre único para el archivo
        String extension = imagen.getOriginalFilename();
        extension = extension.substring(extension.lastIndexOf("."));
        String nombreArchivo = "cliente_" + id + "_" + UUID.randomUUID() + extension;
        
        // Guardar archivo
        Path rutaArchivo = directorioPath.resolve(nombreArchivo);
        Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        
        // Actualizar registro
        cliente.setImagen(nombreArchivo);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        return ClienteMapper.mapToClienteDto(clienteActualizado);
    }
    
    @Override
    @Transactional
    public ClienteDto eliminarImagenPerfil(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        
        // Eliminar archivo físico si existe
        if (cliente.getImagen() != null) {
            try {
                Path rutaArchivo = Paths.get(uploadDir).resolve(cliente.getImagen());
                Files.deleteIfExists(rutaArchivo);
            } catch (IOException e) {
                System.err.println("No se pudo eliminar el archivo de imagen: " + e.getMessage());
            }
        }
        
        // Actualizar registro
        cliente.setImagen(null);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        return ClienteMapper.mapToClienteDto(clienteActualizado);
    }
}