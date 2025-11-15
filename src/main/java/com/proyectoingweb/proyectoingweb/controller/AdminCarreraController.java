package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Carrera;
import com.proyectoingweb.proyectoingweb.service.CarreraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/carreras")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminCarreraController {

    @Autowired
    private CarreraService carreraService;

    // Obtener todas las carreras
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCarreras() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Carrera> carreras = carreraService.getAllCarreras();
            response.put("success", true);
            response.put("carreras", carreras);
            response.put("total", carreras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener carreras: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener carrera por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCarreraById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Carrera> carrera = carreraService.getCarreraById(id);
            if (carrera.isPresent()) {
                response.put("success", true);
                response.put("carrera", carrera.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Carrera no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener carrera: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear carrera
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCarrera(@RequestBody CarreraRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Carrera carrera = new Carrera(request.getCodigo(), request.getNombre(), request.getDescripcion());
            Carrera saved = carreraService.createCarrera(carrera);
            
            response.put("success", true);
            response.put("message", "Carrera creada exitosamente");
            response.put("carrera", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear carrera: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar carrera
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCarrera(@PathVariable Long id, @RequestBody CarreraRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Carrera carreraActualizada = new Carrera(request.getCodigo(), request.getNombre(), request.getDescripcion());
            Carrera updated = carreraService.updateCarrera(id, carreraActualizada);
            
            response.put("success", true);
            response.put("message", "Carrera actualizada exitosamente");
            response.put("carrera", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar carrera: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar carrera
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCarrera(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            carreraService.deleteCarrera(id);
            response.put("success", true);
            response.put("message", "Carrera eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar carrera: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Validar si una carrera tiene todos sus pesos configurados
    @GetMapping("/{id}/validar")
    public ResponseEntity<Map<String, Object>> validarCarrera(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean completa = carreraService.isCarreraCompleta(id);
            response.put("success", true);
            response.put("completa", completa);
            
            if (!completa) {
                try {
                    carreraService.validarPesosCarrera(id);
                } catch (RuntimeException e) {
                    response.put("mensaje", e.getMessage());
                }
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al validar carrera: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class CarreraRequest {
        private String codigo;
        private String nombre;
        private String descripcion;

        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}
