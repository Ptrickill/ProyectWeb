package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.User;
import com.proyectoingweb.proyectoingweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Controlador
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserService userService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar usuario por username
            Optional<User> usuarioOpt = userService.getUserByUsername(loginRequest.getUsername());
            
            if (usuarioOpt.isPresent()) {
                User usuario = usuarioOpt.get();
                
                // Verificar contraseña
                if (usuario.getPassword().equals(loginRequest.getPassword())) {
                    // Login exitoso
                    response.put("success", true);
                    response.put("message", "Login exitoso");
                    
                    // Crear datos del usuario
                    Map<String, Object> usuarioData = new HashMap<>();
                    usuarioData.put("id", usuario.getId());
                    usuarioData.put("username", usuario.getUsername());
                    usuarioData.put("email", usuario.getEmail());
                    usuarioData.put("nombreCompleto", usuario.getNombreCompleto());
                    usuarioData.put("role", usuario.getRole().toString());
                    
                    response.put("usuario", usuarioData);
                    return ResponseEntity.ok(response);
                }
            }
            
            // Login fallido
            response.put("success", false);
            response.put("message", "Usuario o contraseña incorrectos");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error del servidor: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Registro
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificacion si el usuario ya existe
            if (userService.existsByUsername(registerRequest.getUsername())) {
                response.put("success", false);
                response.put("message", "El usuario ya existe");
                return ResponseEntity.ok(response);
            }

            if (userService.existsByEmail(registerRequest.getEmail())) {
                response.put("success", false);
                response.put("message", "El email ya está registrado");
                return ResponseEntity.ok(response);
            }

            // Creacion nuevo usuario
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setNombreCompleto(registerRequest.getNombreCompleto());
            newUser.setRole(User.Role.USER);

            User savedUser = userService.createUser(newUser);

            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            
            // Crear datos del usuario
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", savedUser.getId());
            usuarioData.put("username", savedUser.getUsername());
            usuarioData.put("email", savedUser.getEmail());
            usuarioData.put("nombreCompleto", savedUser.getNombreCompleto());
            usuarioData.put("role", savedUser.getRole().toString());
            
            response.put("usuario", usuarioData);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clases para Requests
    
    // Clase para recibir datos de login
    public static class LoginRequest {
        private String username;
        private String password;
 
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Clase para recibir datos de registro
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String nombreCompleto;

        public RegisterRequest() {}

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    }
}