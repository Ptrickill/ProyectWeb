package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Materia;
import com.proyectoingweb.proyectoingweb.service.MateriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/materias")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminMateriaController {

    @Autowired
    private MateriaService materiaService;

    // Obtener todas las materias
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMaterias() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Materia> materias = materiaService.getAllMaterias();
            response.put("success", true);
            response.put("materias", materias);
            response.put("total", materias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener materias: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener materia por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMateriaById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Materia> materia = materiaService.getMateriaById(id);
            if (materia.isPresent()) {
                response.put("success", true);
                response.put("materia", materia.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Materia no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener materia: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener materias por área
    @GetMapping("/area/{area}")
    public ResponseEntity<Map<String, Object>> getMateriasByArea(@PathVariable String area) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Materia> materias = materiaService.getMateriasByArea(area);
            response.put("success", true);
            response.put("materias", materias);
            response.put("total", materias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener materias: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear materia
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMateria(@RequestBody MateriaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Materia materia = new Materia(request.getNombre(), request.getArea());
            Materia saved = materiaService.createMateria(materia);
            
            response.put("success", true);
            response.put("message", "Materia creada exitosamente");
            response.put("materia", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear materia: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar materia
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMateria(@PathVariable Long id, @RequestBody MateriaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Materia materiaActualizada = new Materia(request.getNombre(), request.getArea());
            Materia updated = materiaService.updateMateria(id, materiaActualizada);
            
            response.put("success", true);
            response.put("message", "Materia actualizada exitosamente");
            response.put("materia", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar materia: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar materia
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMateria(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            materiaService.deleteMateria(id);
            response.put("success", true);
            response.put("message", "Materia eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar materia: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class MateriaRequest {
        private String nombre;
        private String area;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
    }
}
