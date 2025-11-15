package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiante")
@CrossOrigin(origins = "http://localhost:4200")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    // Crear perfil de estudiante
    @PostMapping("/perfil")
    public ResponseEntity<Map<String, Object>> crearPerfil(@RequestBody PerfilRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudiante = new Estudiante(request.getNombre(), request.getEdad());
            Estudiante saved = estudianteService.createEstudiante(estudiante);
            
            response.put("success", true);
            response.put("message", "Perfil creado exitosamente");
            response.put("estudiante", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener perfil por ID
    @GetMapping("/perfil/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPerfil(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Estudiante> estudiante = estudianteService.getEstudianteById(id);
            if (estudiante.isPresent()) {
                response.put("success", true);
                response.put("estudiante", estudiante.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar perfil
    @PutMapping("/perfil/{id}")
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @PathVariable Long id, 
            @RequestBody PerfilRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudianteActualizado = new Estudiante(request.getNombre(), request.getEdad());
            Estudiante updated = estudianteService.updateEstudiante(id, estudianteActualizado);
            
            response.put("success", true);
            response.put("message", "Perfil actualizado exitosamente");
            response.put("estudiante", updated);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener todos los estudiantes (para admin)
    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Estudiante> estudiantes = estudianteService.getAllEstudiantes();
            response.put("success", true);
            response.put("estudiantes", estudiantes);
            response.put("total", estudiantes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estudiantes: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class PerfilRequest {
        private String nombre;
        private Integer edad;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
    }
}
