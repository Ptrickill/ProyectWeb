package com.proyectoingweb.proyectoingweb.factory;

import com.proyectoingweb.proyectoingweb.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Patrón Factory: Centraliza la creación de objetos User con lógica compleja.
 * Permite crear diferentes tipos de usuarios con configuraciones predefinidas.
 * 
 * Beneficios:
 * - Encapsula la lógica de creación
 * - Facilita cambios futuros en la construcción de usuarios
 * - Provee métodos factory específicos para cada tipo de usuario
 */
@Component
public class UserFactory {
    
    /**
     * Crea un usuario básico con valores por defecto.
     */
    public User createUser(String username, String password, String email, String nombreCompleto) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNombreCompleto(nombreCompleto);
        user.setRole(User.Role.USER);
        user.setEnabled(true);
        user.setFechaCreacion(LocalDateTime.now());
        return user;
    }
    
    /**
     * Crea un usuario administrador.
     */
    public User createAdmin(String username, String password, String email, String nombreCompleto) {
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setNombreCompleto(nombreCompleto);
        admin.setRole(User.Role.ADMIN);
        admin.setEnabled(true);
        admin.setFechaCreacion(LocalDateTime.now());
        return admin;
    }
    
    /**
     * Crea un usuario deshabilitado (para cuentas temporales o suspendidas).
     */
    public User createDisabledUser(String username, String password, String email, String nombreCompleto) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNombreCompleto(nombreCompleto);
        user.setRole(User.Role.USER);
        user.setEnabled(false);
        user.setFechaCreacion(LocalDateTime.now());
        return user;
    }
    
    /**
     * Crea un usuario con rol específico.
     */
    public User createUserWithRole(String username, String password, String email, 
                                   String nombreCompleto, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNombreCompleto(nombreCompleto);
        user.setRole(role);
        user.setEnabled(true);
        user.setFechaCreacion(LocalDateTime.now());
        return user;
    }
    
    /**
     * Crea un usuario de prueba para desarrollo/testing.
     */
    public User createTestUser(String suffix) {
        User user = new User();
        user.setUsername("test_user_" + suffix);
        user.setPassword("test123");
        user.setEmail("test_" + suffix + "@test.com");
        user.setNombreCompleto("Usuario de Prueba " + suffix);
        user.setRole(User.Role.USER);
        user.setEnabled(true);
        user.setFechaCreacion(LocalDateTime.now());
        return user;
    }
}
