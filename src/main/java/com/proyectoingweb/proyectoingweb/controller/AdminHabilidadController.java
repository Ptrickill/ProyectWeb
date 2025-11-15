package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Habilidad;
import com.proyectoingweb.proyectoingweb.service.HabilidadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/habilidades")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminHabilidadController {

    @Autowired
    private HabilidadService habilidadService;

    // Obtener todas las habilidades
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHabilidades() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Habilidad> habilidades = habilidadService.getAllHabilidades();
            response.put("success", true);
            response.put("habilidades", habilidades);
            response.put("total", habilidades.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener habilidades: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener habilidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHabilidadById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Habilidad> habilidad = habilidadService.getHabilidadById(id);
            if (habilidad.isPresent()) {
                response.put("success", true);
                response.put("habilidad", habilidad.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Habilidad no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener habilidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear habilidad
    @PostMapping
    public ResponseEntity<Map<String, Object>> createHabilidad(@RequestBody HabilidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Habilidad habilidad = new Habilidad(request.getNombre(), request.getDescripcion());
            Habilidad saved = habilidadService.createHabilidad(habilidad);
            
            response.put("success", true);
            response.put("message", "Habilidad creada exitosamente");
            response.put("habilidad", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear habilidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar habilidad
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHabilidad(@PathVariable Long id, @RequestBody HabilidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Habilidad habilidadActualizada = new Habilidad(request.getNombre(), request.getDescripcion());
            Habilidad updated = habilidadService.updateHabilidad(id, habilidadActualizada);
            
            response.put("success", true);
            response.put("message", "Habilidad actualizada exitosamente");
            response.put("habilidad", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar habilidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar habilidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHabilidad(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            habilidadService.deleteHabilidad(id);
            response.put("success", true);
            response.put("message", "Habilidad eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar habilidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Validar si una habilidad está completa (tiene al menos 2 preguntas)
    @GetMapping("/{id}/validar")
    public ResponseEntity<Map<String, Object>> validarHabilidad(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean completa = habilidadService.isHabilidadCompleta(id);
            response.put("success", true);
            response.put("completa", completa);
            
            if (!completa) {
                try {
                    habilidadService.validarHabilidadActiva(id);
                } catch (RuntimeException e) {
                    response.put("mensaje", e.getMessage());
                }
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al validar habilidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class HabilidadRequest {
        private String nombre;
        private String descripcion;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}
