package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.repository.NotaRepository;
import com.proyectoingweb.proyectoingweb.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reportes")
public class ReporteController {

    @Autowired
    private NotaRepository notaRepository;
    
    @Autowired
    private ResultadoRepository resultadoRepository;

    // Reporte de materias más cursadas
    @GetMapping("/materias-populares")
    public ResponseEntity<Map<String, Object>> getMateriasPopulares() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Query nativa para contar estudiantes por materia
            List<Object[]> resultados = notaRepository.findMateriasPopulares();
            
            response.put("success", true);
            response.put("data", resultados);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Reporte de carreras más recomendadas (basado en resultados vocacionales)
    @GetMapping("/carreras-recomendadas")
    public ResponseEntity<Map<String, Object>> getCarrerasRecomendadas() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Query para obtener las carreras que más aparecen en los top 3 de cada estudiante
            List<Object[]> resultados = resultadoRepository.findCarrerasRecomendadas();
            
            response.put("success", true);
            response.put("data", resultados);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
