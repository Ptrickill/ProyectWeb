package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Afinidad;
import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.service.AfinidadService;
import com.proyectoingweb.proyectoingweb.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiante/afinidades")
@CrossOrigin(origins = "http://localhost:4200")
public class AfinidadController {

    @Autowired
    private AfinidadService afinidadService;
    
    @Autowired
    private EstudianteService estudianteService;

    // Obtener afinidades de un estudiante
    @GetMapping("/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerAfinidades(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Afinidad> afinidades = afinidadService.getAfinidadesByEstudiante(estudianteId);
            long total = afinidadService.countAfinidadesByEstudiante(estudianteId);
            
            response.put("success", true);
            response.put("afinidades", afinidades);
            response.put("total", total);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener afinidades: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear afinidad
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearAfinidad(@RequestBody AfinidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar estudiante
            Optional<Estudiante> estudianteOpt = estudianteService.getEstudianteById(request.getEstudianteId());
            if (!estudianteOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.ok(response);
            }
            
            // Crear afinidad
            Afinidad afinidad = new Afinidad(request.getArea(), estudianteOpt.get());
            Afinidad saved = afinidadService.createAfinidad(afinidad);
            
            response.put("success", true);
            response.put("message", "Afinidad registrada exitosamente");
            response.put("afinidad", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear afinidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar afinidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarAfinidad(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            afinidadService.deleteAfinidad(id);
            response.put("success", true);
            response.put("message", "Afinidad eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar afinidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener áreas disponibles
    @GetMapping("/areas")
    public ResponseEntity<Map<String, Object>> obtenerAreas() {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] areas = {"Matemáticas", "Ciencias", "Lengua", "Sociales", "Arte", "Tecnología"};
            
            response.put("success", true);
            response.put("areas", areas);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener áreas: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class AfinidadRequest {
        private Long estudianteId;
        private String area;

        public Long getEstudianteId() { return estudianteId; }
        public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
    }
}
