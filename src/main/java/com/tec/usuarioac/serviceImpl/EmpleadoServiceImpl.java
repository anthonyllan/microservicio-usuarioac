package com.tec.usuarioac.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tec.usuarioac.dto.EmpleadoDto;
import com.tec.usuarioac.dto.UsuarioDto;
import com.tec.usuarioac.entity.Empleado;
import com.tec.usuarioac.entity.Usuario;
import com.tec.usuarioac.exception.ResourceNotFoundException;
import com.tec.usuarioac.mapper.EmpleadoMapper;
import com.tec.usuarioac.repository.EmpleadoRepository;
import com.tec.usuarioac.repository.EmpleadoRolRepository;
import com.tec.usuarioac.repository.UsuarioRepository;
import com.tec.usuarioac.service.EmpleadoService;
import com.tec.usuarioac.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final EmpleadoRolRepository empleadoRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.upload.dir.empleados:/uploads/empleados}")
    private String uploadDir;

    @Override
    @Transactional
    public EmpleadoDto registrarEmpleado(EmpleadoDto empleadoDto) {
        // Verificar si el correo ya existe
        if (empleadoDto.getUsuario() != null && 
            usuarioService.existeUsuarioPorCorreo(empleadoDto.getUsuario().getCorreo())) {
            throw new ResourceNotFoundException("El correo ya está registrado");
        }
        
        // Guardar usuario primero
        UsuarioDto usuarioDto = usuarioService.guardarUsuario(empleadoDto.getUsuario());
        empleadoDto.setUsuario(usuarioDto);
        
        // Guardar empleado
        Empleado empleado = EmpleadoMapper.mapToEmpleado(empleadoDto);
        Empleado nuevoEmpleado = empleadoRepository.save(empleado);
        
        return EmpleadoMapper.mapToEmpleadoDto(nuevoEmpleado);
    }
    
    @Override
    public EmpleadoDto obtenerEmpleadoPorId(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return EmpleadoMapper.mapToEmpleadoDto(empleado);
    }

    @Override
    public List<EmpleadoDto> listarEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAll();
        return empleados.stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }
    
    // ========== IMPLEMENTACIONES NUEVAS PARA PERFIL ==========
    
    @Override
    @Transactional
    public EmpleadoDto actualizarEmpleado(Long id, EmpleadoDto empleadoDto) {
        System.out.println("🔧 [EmpleadoService] Actualizando empleado ID: " + id);
        
        // Buscar empleado existente
        Empleado empleadoExistente = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        
        // Actualizar campos del empleado
        if (empleadoDto.getNombre() != null) {
            empleadoExistente.setNombre(empleadoDto.getNombre());
        }
        if (empleadoDto.getApellidos() != null) {
            empleadoExistente.setApellidos(empleadoDto.getApellidos());
        }
        if (empleadoDto.getTelefono() != null) {
            empleadoExistente.setTelefono(empleadoDto.getTelefono());
        }
        
        // Actualizar datos del usuario si se proporcionan
        if (empleadoDto.getUsuario() != null) {
            Usuario usuario = empleadoExistente.getUsuario();
            
            // Solo actualizar correo si cambió y no existe ya
            if (empleadoDto.getUsuario().getCorreo() != null && 
                !empleadoDto.getUsuario().getCorreo().equals(usuario.getCorreo())) {
                
                if (usuarioService.existeUsuarioPorCorreo(empleadoDto.getUsuario().getCorreo())) {
                    throw new ResourceNotFoundException("El correo ya está registrado");
                }
                usuario.setCorreo(empleadoDto.getUsuario().getCorreo());
                System.out.println("📧 [EmpleadoService] Correo actualizado a: " + usuario.getCorreo());
            }
            
            // ✅ ENCRIPTAR CONTRASEÑA SI SE PROPORCIONA
            if (empleadoDto.getUsuario().getContrasena() != null && 
                !empleadoDto.getUsuario().getContrasena().isEmpty()) {
                
                String contrasenaPlana = empleadoDto.getUsuario().getContrasena();
                
                // Verificar si ya está encriptada (comienza con $2a$)
                if (contrasenaPlana.startsWith("$2a$")) {
                    System.out.println("ℹ️  [EmpleadoService] Contraseña ya encriptada, no se modifica");
                } else {
                    // Encriptar la contraseña
                    String contrasenaEncriptada = passwordEncoder.encode(contrasenaPlana);
                    usuario.setContrasena(contrasenaEncriptada);
                    System.out.println("🔐 [EmpleadoService] Contraseña encriptada correctamente");
                    System.out.println("📝 [EmpleadoService] Hash generado: " + contrasenaEncriptada.substring(0, 20) + "...");
                }
            }
            
            usuarioRepository.save(usuario);
            System.out.println("✅ [EmpleadoService] Usuario actualizado correctamente");
        }
        
        // Guardar cambios
        Empleado empleadoActualizado = empleadoRepository.save(empleadoExistente);
        System.out.println("✅ [EmpleadoService] Empleado actualizado exitosamente");
        
        return EmpleadoMapper.mapToEmpleadoDto(empleadoActualizado);
    }
    
    @Override
    @Transactional
    public EmpleadoDto actualizarImagenPerfil(Long id, MultipartFile imagen) throws IOException {
        // Buscar empleado existente
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        
        // Validar archivo
        if (imagen.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        // Validar tipo de archivo
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        
        // Eliminar imagen anterior si existe
        if (empleado.getImagen() != null && !empleado.getImagen().isEmpty()) {
            try {
                Path imagenAnterior = Paths.get(uploadDir, empleado.getImagen());
                Files.deleteIfExists(imagenAnterior);
            } catch (IOException e) {
                // Log error pero continuar
                System.err.println("Error al eliminar imagen anterior: " + e.getMessage());
            }
        }
        
        // Crear directorio si no existe
        Path directorioPath = Paths.get(uploadDir);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath);
        }
        
        // Generar nombre único para la imagen
        String extension = obtenerExtension(imagen.getOriginalFilename());
        String nombreArchivo = "empleado_" + id + "_" + UUID.randomUUID().toString() + extension;
        
        // Guardar archivo
        Path rutaArchivo = directorioPath.resolve(nombreArchivo);
        Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        
        // Actualizar empleado con la ruta de la imagen
        empleado.setImagen(nombreArchivo);
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        
        return EmpleadoMapper.mapToEmpleadoDto(empleadoActualizado);
    }
    
    @Override
    @Transactional
    public EmpleadoDto eliminarImagenPerfil(Long id) {
        // Buscar empleado existente
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        
        // Eliminar archivo físico si existe
        if (empleado.getImagen() != null && !empleado.getImagen().isEmpty()) {
            try {
                Path imagenPath = Paths.get(uploadDir, empleado.getImagen());
                Files.deleteIfExists(imagenPath);
            } catch (IOException e) {
                // Log error pero continuar
                System.err.println("Error al eliminar imagen: " + e.getMessage());
            }
        }
        
        // Actualizar empleado removiendo la referencia a la imagen
        empleado.setImagen(null);
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        
        return EmpleadoMapper.mapToEmpleadoDto(empleadoActualizado);
    }
    
    @Override
    @Transactional
    public void eliminarEmpleado(Long id) {
        System.out.println("🗑️  [EmpleadoService] Eliminando empleado ID: " + id);
        
        // Buscar empleado existente
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        
        // 1. Eliminar primero todos los roles asignados (para evitar error de FK)
        empleadoRolRepository.deleteByEmpleadoId(id);
        System.out.println("✅ [EmpleadoService] Roles del empleado eliminados");
        
        // 2. Eliminar la imagen del empleado si existe
        if (empleado.getImagen() != null && !empleado.getImagen().isEmpty()) {
            try {
                Path imagenPath = Paths.get(uploadDir, empleado.getImagen());
                Files.deleteIfExists(imagenPath);
                System.out.println("✅ [EmpleadoService] Imagen del empleado eliminada");
            } catch (IOException e) {
                // Log error pero continuar con la eliminación
                System.err.println("⚠️  Error al eliminar imagen del empleado: " + e.getMessage());
            }
        }
        
        // 3. Eliminar el empleado
        empleadoRepository.delete(empleado);
        System.out.println("✅ [EmpleadoService] Empleado eliminado exitosamente");
    }
    
    /**
     * Obtener extensión del archivo
     */
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return ".jpg"; // Extensión por defecto
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }
}