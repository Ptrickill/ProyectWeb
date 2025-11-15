package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Habilidad;
import com.proyectoingweb.proyectoingweb.entity.Pregunta;
import com.proyectoingweb.proyectoingweb.service.HabilidadService;
import com.proyectoingweb.proyectoingweb.service.PreguntaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/preguntas")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminPreguntaController {

    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private HabilidadService habilidadService;

    // Obtener todas las preguntas
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPreguntas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Pregunta> preguntas = preguntaService.getAllPreguntas();
            response.put("success", true);
            response.put("preguntas", preguntas);
            response.put("total", preguntas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener preguntas: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener pregunta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPreguntaById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Pregunta> pregunta = preguntaService.getPreguntaById(id);
            if (pregunta.isPresent()) {
                response.put("success", true);
                response.put("pregunta", pregunta.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Pregunta no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener pregunta: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener preguntas por habilidad
    @GetMapping("/habilidad/{habilidadId}")
    public ResponseEntity<Map<String, Object>> getPreguntasByHabilidad(@PathVariable Long habilidadId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Pregunta> preguntas = preguntaService.getPreguntasByHabilidad(habilidadId);
            response.put("success", true);
            response.put("preguntas", preguntas);
            response.put("total", preguntas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener preguntas: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear pregunta
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPregunta(@RequestBody PreguntaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar habilidad
            Optional<Habilidad> habilidadOpt = habilidadService.getHabilidadById(request.getHabilidadId());
            if (!habilidadOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Habilidad no encontrada");
                return ResponseEntity.ok(response);
            }
            
            Pregunta pregunta = new Pregunta(request.getTexto(), request.getTipoPregunta(), habilidadOpt.get());
            Pregunta saved = preguntaService.createPregunta(pregunta);
            
            response.put("success", true);
            response.put("message", "Pregunta creada exitosamente");
            response.put("pregunta", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear pregunta: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar pregunta
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePregunta(@PathVariable Long id, @RequestBody PreguntaUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pregunta preguntaActualizada = new Pregunta();
            preguntaActualizada.setTexto(request.getTexto());
            preguntaActualizada.setTipoPregunta(request.getTipoPregunta());
            
            Pregunta updated = preguntaService.updatePregunta(id, preguntaActualizada);
            
            response.put("success", true);
            response.put("message", "Pregunta actualizada exitosamente");
            response.put("pregunta", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar pregunta: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar pregunta
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePregunta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            preguntaService.deletePregunta(id);
            response.put("success", true);
            response.put("message", "Pregunta eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar pregunta: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clases para requests
    public static class PreguntaRequest {
        private String texto;
        private String tipoPregunta;
        private Long habilidadId;

        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
        public String getTipoPregunta() { return tipoPregunta; }
        public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }
        public Long getHabilidadId() { return habilidadId; }
        public void setHabilidadId(Long habilidadId) { this.habilidadId = habilidadId; }
    }

    public static class PreguntaUpdateRequest {
        private String texto;
        private String tipoPregunta;

        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
        public String getTipoPregunta() { return tipoPregunta; }
        public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }
    }
}
