package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Resultado;
import com.proyectoingweb.proyectoingweb.service.CalculoVocacionalService;
import com.proyectoingweb.proyectoingweb.service.ResultadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiante/resultados")
@CrossOrigin(origins = "http://localhost:4200")
public class ResultadoController {

    @Autowired
    private CalculoVocacionalService calculoService;
    
    @Autowired
    private ResultadoService resultadoService;

    // ⭐ CALCULAR RESULTADOS VOCACIONALES (El método más importante)
    @PostMapping("/calcular/{estudianteId}")
    public ResponseEntity<Map<String, Object>> calcularResultados(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Calcular y guardar resultados para todas las carreras
            List<Resultado> resultados = calculoService.calcularYGuardarResultados(estudianteId);
            
            response.put("success", true);
            response.put("message", "Resultados calculados exitosamente");
            response.put("resultados", resultados);
            response.put("total", resultados.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al calcular resultados: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener Top 3 de carreras recomendadas
    @GetMapping("/top3/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerTop3(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Resultado> top3 = calculoService.obtenerTop3Carreras(estudianteId);
            
            response.put("success", true);
            response.put("top3", top3);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener Top 3: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener todos los resultados de un estudiante
    @GetMapping("/{estudianteId}")
    public ResponseEntity<Map<String, Object>> obtenerResultados(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Resultado> resultados = resultadoService.getResultadosByEstudiante(estudianteId);
            
            response.put("success", true);
            response.put("resultados", resultados);
            response.put("total", resultados.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener resultados: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Recalcular resultados
    @PostMapping("/recalcular/{estudianteId}")
    public ResponseEntity<Map<String, Object>> recalcularResultados(@PathVariable Long estudianteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Resultado> resultados = calculoService.recalcularResultados(estudianteId);
            
            response.put("success", true);
            response.put("message", "Resultados recalculados exitosamente");
            response.put("resultados", resultados);
            response.put("total", resultados.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al recalcular resultados: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener detalle de un resultado específico
    @GetMapping("/detalle/{resultadoId}")
    public ResponseEntity<Map<String, Object>> obtenerDetalle(@PathVariable Long resultadoId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var resultado = resultadoService.getResultadoById(resultadoId);
            
            if (resultado.isPresent()) {
                Resultado r = resultado.get();
                
                // Preparar detalle con componentes desglosados
                Map<String, Object> detalle = new HashMap<>();
                detalle.put("carrera", r.getCarrera().getNombre());
                detalle.put("descripcion", r.getCarrera().getDescripcion());
                detalle.put("puntajeFinal", r.getPuntajeFinal());
                detalle.put("puntajeAcademico", r.getPuntajeAcademico());
                detalle.put("puntajeHabilidades", r.getPuntajeHabilidades());
                detalle.put("puntajeAfinidad", r.getPuntajeAfinidad());
                detalle.put("fechaCalculo", r.getFechaCalculo());
                
                response.put("success", true);
                response.put("detalle", detalle);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Resultado no encontrado");
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener detalle: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
