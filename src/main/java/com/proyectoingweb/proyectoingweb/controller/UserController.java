package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.User;
import com.proyectoingweb.proyectoingweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    // Usuarios
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            response.put("success", true);
            response.put("users", users);
            response.put("total", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Ussuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                response.put("success", true);
                response.put("usuario", user.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado con ID: " + id);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Creacion Usuario
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar si el usuario ya existe
            if (userService.existsByUsername(request.getUsername())) {
                response.put("success", false);
                response.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.ok(response);
            }

            if (userService.existsByEmail(request.getEmail())) {
                response.put("success", false);
                response.put("message", "El email ya está registrado");
                return ResponseEntity.ok(response);
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(request.getPassword());
            newUser.setEmail(request.getEmail());
            newUser.setNombreCompleto(request.getNombreCompleto());
            newUser.setRole(request.getRole());
            newUser.setEnabled(request.getEnabled());

            User savedUser = userService.createUser(newUser);

            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("usuario", savedUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Edicion
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verificar si el usuario existe
            Optional<User> existingUserOpt = userService.getUserById(id);
            if (!existingUserOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado con ID: " + id);
                return ResponseEntity.ok(response);
            }

            User existingUser = existingUserOpt.get();

            // Validar username único
            if (!existingUser.getUsername().equals(request.getUsername()) &&
                userService.existsByUsername(request.getUsername())) {
                response.put("success", false);
                response.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.ok(response);
            }

            // Validar email único
            if (!existingUser.getEmail().equals(request.getEmail()) &&
                userService.existsByEmail(request.getEmail())) {
                response.put("success", false);
                response.put("message", "El email ya está registrado");
                return ResponseEntity.ok(response);
            }

            // Actualizar usuario
            User userToUpdate = new User();
            userToUpdate.setUsername(request.getUsername());
            userToUpdate.setEmail(request.getEmail());
            userToUpdate.setNombreCompleto(request.getNombreCompleto());
            userToUpdate.setRole(request.getRole());
            userToUpdate.setEnabled(request.getEnabled());
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                userToUpdate.setPassword(request.getPassword());
            }

            User updatedUser = userService.updateUser(id, userToUpdate);

            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("usuario", updatedUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Elliminar Usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> user = userService.getUserById(id);
            if (!user.isPresent()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado con ID: " + id);
                return ResponseEntity.ok(response);
            }

            userService.deleteUser(id);
            
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clases para crear y actualizar

    public static class UserCreateRequest {
        private String username;
        private String password;
        private String email;
        private String nombreCompleto;
        private User.Role role = User.Role.USER;
        private Boolean enabled = true;

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    public static class UserUpdateRequest {
        private String username;
        private String password;
        private String email;
        private String nombreCompleto;
        private User.Role role;
        private Boolean enabled;

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }
}