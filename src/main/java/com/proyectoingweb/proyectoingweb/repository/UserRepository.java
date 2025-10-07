package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Método para buscar por uesrname
    Optional<User> findByUsername(String username);
    
    // Buscar por email
    Optional<User> findByEmail(String email);
    
    // Verificar si existe username
    boolean existsByUsername(String username);
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar usuarios activos
    List<User> findByEnabledTrue();
    
    // Buscar por role
    List<User> findByRole(User.Role role);
    
    // Buscar por nombre completo (contiene texto)
    List<User> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);
    
    // Query personalizada: usuarios activos ordenados por fecha
    @Query("SELECT u FROM User u WHERE u.enabled = true ORDER BY u.fechaCreacion DESC")
    List<User> findActiveUsersOrderByDate();
}