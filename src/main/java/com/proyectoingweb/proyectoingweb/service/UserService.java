package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.User;
import com.proyectoingweb.proyectoingweb.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Servicio SIMPLIFICADO para usuarios - Sin encriptación de contraseñas
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ========== OPERACIONES CRUD SIMPLES ==========

    // CREATE - Crear usuario (contraseña sin encriptar)
    public User createUser(User user) {
        // Establecer fecha de creación
        user.setFechaCreacion(LocalDateTime.now());
        // Guardar usuario con contraseña en texto plano (SIMPLE)
        return userRepository.save(user);
    }

    // READ - Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ - Obtener usuario por ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // READ - Obtener usuario por username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // UPDATE - Actualizar usuario
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setNombreCompleto(userDetails.getNombreCompleto());
        user.setRole(userDetails.getRole());
        user.setEnabled(userDetails.getEnabled());

        // Solo actualizar contraseña si se proporciona una nueva (sin encriptar)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }

        return userRepository.save(user);
    }

    // DELETE - Eliminar usuario
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
    }

    // ========== MÉTODOS DE VALIDACIÓN ==========

    // Verificar si username ya existe
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Verificar si email ya existe
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ========== MÉTODOS DE BÚSQUEDA ==========

    // Obtener usuarios activos
    public List<User> getActiveUsers() {
        return userRepository.findByEnabledTrue();
    }

    // Buscar usuarios por role
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    // Buscar por nombre completo
    public List<User> searchByNombre(String nombre) {
        return userRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    // ========== MÉTODOS ESPECIALES ==========

    // Cambiar estado de usuario (activar/desactivar)
    public User toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }

    // Cambiar contraseña (comparación simple)
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Verificar contraseña actual (comparación simple)
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // Contar usuarios
    public long countUsers() {
        return userRepository.count();
    }

    // Contar usuarios activos
    public long countActiveUsers() {
        return userRepository.findByEnabledTrue().size();
    }
}