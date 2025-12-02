package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Afinidad;
import com.proyectoingweb.proyectoingweb.entity.Carrera;
import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.service.AfinidadService;
import com.proyectoingweb.proyectoingweb.service.CarreraService;
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

public class AfinidadController {

    @Autowired
    private AfinidadService afinidadService;
    
    @Autowired
    private EstudianteService estudianteService;
    
    @Autowired
    private CarreraService carreraService;

    // Obtener afinidades de un estudiante
    @GetMapping("/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerAfinidades(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Afinidad> afinidades = afinidadService.getAfinidadesByEstudiante(estudianteId);
            long total = afinidadService.countAfinidadesByEstudiante(estudianteId);
            
            response.put("success", true);
            response.put("data", afinidades);
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
            
            // Buscar carrera
            Optional<Carrera> carreraOpt = carreraService.getCarreraById(request.getCarreraId());
            if (!carreraOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Carrera no encontrada");
                return ResponseEntity.ok(response);
            }
            
            // Crear afinidad
            Afinidad afinidad = new Afinidad(request.getNivelInteres(), estudianteOpt.get(), carreraOpt.get());
            Afinidad saved = afinidadService.createAfinidad(afinidad);
            
            response.put("success", true);
            response.put("message", "Afinidad registrada exitosamente");
            response.put("data", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear afinidad: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // Actualizar afinidad
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarAfinidad(
            @PathVariable Long id,
            @RequestBody AfinidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Afinidad> afinidadOpt = afinidadService.getAfinidadById(id);
            if (!afinidadOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Afinidad no encontrada");
                return ResponseEntity.ok(response);
            }
            
            Afinidad afinidad = afinidadOpt.get();
            afinidad.setNivelInteres(request.getNivelInteres());
            
            Afinidad updated = afinidadService.updateAfinidad(id, afinidad);
            
            response.put("success", true);
            response.put("message", "Afinidad actualizada exitosamente");
            response.put("data", updated);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar afinidad: " + e.getMessage());
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

    // Clase para request
    public static class AfinidadRequest {
        private Long estudianteId;
        private Long carreraId;
        private Integer nivelInteres;

        public Long getEstudianteId() { return estudianteId; }
        public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
        public Long getCarreraId() { return carreraId; }
        public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
        public Integer getNivelInteres() { return nivelInteres; }
        public void setNivelInteres(Integer nivelInteres) { this.nivelInteres = nivelInteres; }
    }
}
