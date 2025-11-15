package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.entity.Pregunta;
import com.proyectoingweb.proyectoingweb.entity.RespuestaHabilidad;
import com.proyectoingweb.proyectoingweb.service.EstudianteService;
import com.proyectoingweb.proyectoingweb.service.PreguntaService;
import com.proyectoingweb.proyectoingweb.service.RespuestaHabilidadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiante/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestHabilidadController {

    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private RespuestaHabilidadService respuestaService;
    
    @Autowired
    private EstudianteService estudianteService;

    // Obtener todas las preguntas del test
    @GetMapping("/preguntas")
    public ResponseEntity<Map<String, Object>> obtenerPreguntas() {
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

    // Guardar respuesta
    @PostMapping("/respuestas")
    public ResponseEntity<Map<String, Object>> guardarRespuesta(@RequestBody RespuestaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar estudiante
            Optional<Estudiante> estudianteOpt = estudianteService.getEstudianteById(request.getEstudianteId());
            if (!estudianteOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.ok(response);
            }
            
            // Buscar pregunta
            Optional<Pregunta> preguntaOpt = preguntaService.getPreguntaById(request.getPreguntaId());
            if (!preguntaOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Pregunta no encontrada");
                return ResponseEntity.ok(response);
            }
            
            // Crear respuesta
            RespuestaHabilidad respuesta = new RespuestaHabilidad(
                request.getPuntaje(),
                estudianteOpt.get(),
                preguntaOpt.get()
            );
            
            RespuestaHabilidad saved = respuestaService.createRespuesta(respuesta);
            
            response.put("success", true);
            response.put("message", "Respuesta guardada exitosamente");
            response.put("respuesta", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al guardar respuesta: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener respuestas de un estudiante
    @GetMapping("/respuestas/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerRespuestas(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<RespuestaHabilidad> respuestas = respuestaService.getRespuestasByEstudiante(estudianteId);
            Float promedio = respuestaService.calcularPromedioHabilidades(estudianteId);
            
            response.put("success", true);
            response.put("respuestas", respuestas);
            response.put("promedio", promedio);
            response.put("total", respuestas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener respuestas: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener progreso del test
    @GetMapping("/progreso/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerProgreso(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Pregunta> totalPreguntas = preguntaService.getAllPreguntas();
            List<RespuestaHabilidad> respuestas = respuestaService.getRespuestasByEstudiante(estudianteId);
            
            int total = totalPreguntas.size();
            int respondidas = respuestas.size();
            float porcentaje = total > 0 ? (respondidas * 100.0f) / total : 0;
            
            response.put("success", true);
            response.put("totalPreguntas", total);
            response.put("respondidas", respondidas);
            response.put("porcentajeCompletado", porcentaje);
            response.put("completado", respondidas == total);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener progreso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clase para request
    public static class RespuestaRequest {
        private Long estudianteId;
        private Long preguntaId;
        private Integer puntaje;

        public Long getEstudianteId() { return estudianteId; }
        public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
        public Long getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Long preguntaId) { this.preguntaId = preguntaId; }
        public Integer getPuntaje() { return puntaje; }
        public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    }
}
