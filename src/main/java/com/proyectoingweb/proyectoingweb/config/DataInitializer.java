package com.proyectoingweb.proyectoingweb.config;

import com.proyectoingweb.proyectoingweb.entity.User;
import com.proyectoingweb.proyectoingweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // usuario adm prueba
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123"); 
            admin.setEmail("admin@ingenieriaweb.com");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            
            userService.createUser(admin);
            System.out.println(" Usuario administrador creado: admin / admin123");
        }
        
        // usuario prueba
        if (!userService.existsByUsername("usuario")) {
            User user = new User();
            user.setUsername("usuario");
            user.setPassword("user123"); 
            user.setEmail("usuario@ingenieriaweb.com");
            user.setNombreCompleto("Usuario de Prueba");
            user.setRole(User.Role.USER);
            user.setEnabled(true);
            
            userService.createUser(user);
            System.out.println(" Usuario de prueba creado: usuario / user123");
        }
    }
}