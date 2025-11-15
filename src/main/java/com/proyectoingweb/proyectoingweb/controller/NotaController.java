package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.entity.Materia;
import com.proyectoingweb.proyectoingweb.entity.Nota;
import com.proyectoingweb.proyectoingweb.service.EstudianteService;
import com.proyectoingweb.proyectoingweb.service.MateriaService;
import com.proyectoingweb.proyectoingweb.service.NotaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiante/notas")
@CrossOrigin(origins = "http://localhost:4200")
public class NotaController {

    @Autowired
    private NotaService notaService;
    
    @Autowired
    private EstudianteService estudianteService;
    
    @Autowired
    private MateriaService materiaService;

    // Obtener notas de un estudiante
    @GetMapping("/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerNotas(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Nota> notas = notaService.getNotasByEstudiante(estudianteId);
            Float promedio = notaService.calcularPromedioAcademico(estudianteId);
            
            response.put("success", true);
            response.put("notas", notas);
            response.put("promedio", promedio);
            response.put("total", notas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener notas: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear nota
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearNota(@RequestBody NotaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar estudiante
            Optional<Estudiante> estudianteOpt = estudianteService.getEstudianteById(request.getEstudianteId());
            if (!estudianteOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.ok(response);
            }
            
            // Buscar materia
            Optional<Materia> materiaOpt = materiaService.getMateriaById(request.getMateriaId());
            if (!materiaOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Materia no encontrada");
                return ResponseEntity.ok(response);
            }
            
            // Crear nota
            Nota nota = new Nota(request.getCalificacion(), estudianteOpt.get(), materiaOpt.get());
            Nota saved = notaService.createNota(nota);
            
            response.put("success", true);
            response.put("message", "Nota registrada exitosamente");
            response.put("nota", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear nota: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar nota
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarNota(
            @PathVariable Long id,
            @RequestBody NotaUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Nota notaActualizada = new Nota();
            notaActualizada.setCalificacion(request.getCalificacion());
            
            Nota updated = notaService.updateNota(id, notaActualizada);
            
            response.put("success", true);
            response.put("message", "Nota actualizada exitosamente");
            response.put("nota", updated);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar nota: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar nota
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarNota(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            notaService.deleteNota(id);
            response.put("success", true);
            response.put("message", "Nota eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar nota: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clases para requests
    public static class NotaRequest {
        private Long estudianteId;
        private Long materiaId;
        private Float calificacion;

        public Long getEstudianteId() { return estudianteId; }
        public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
        public Long getMateriaId() { return materiaId; }
        public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }
        public Float getCalificacion() { return calificacion; }
        public void setCalificacion(Float calificacion) { this.calificacion = calificacion; }
    }

    public static class NotaUpdateRequest {
        private Float calificacion;

        public Float getCalificacion() { return calificacion; }
        public void setCalificacion(Float calificacion) { this.calificacion = calificacion; }
    }
}
