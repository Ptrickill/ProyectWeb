package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para ver datos del sistema desde el navegador (endpoints GET públicos)
 * Accede a: http://localhost:8080/api/view/... o /api/public/...
 */
@RestController
@RequestMapping("/api")

public class PublicViewController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CarreraService carreraService;
    
    @Autowired
    private MateriaService materiaService;
    
    @Autowired
    private HabilidadService habilidadService;
    
    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private EstudianteService estudianteService;

    // ========== ENDPOINTS PÚBLICOS ==========
    
    // Ver todas las carreras (público)
    @GetMapping("/public/carreras")
    public ResponseEntity<Map<String, Object>> getCarrerasPublico() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", carreraService.getAllCarreras());
        return ResponseEntity.ok(response);
    }

    // Ver todas las materias (público)
    @GetMapping("/public/materias")
    public ResponseEntity<Map<String, Object>> getMateriasPublico() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", materiaService.getAllMaterias());
        return ResponseEntity.ok(response);
    }

    // Ver todas las habilidades (público)
    @GetMapping("/public/habilidades")
    public ResponseEntity<Map<String, Object>> getHabilidadesPublico() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", habilidadService.getAllHabilidades());
        return ResponseEntity.ok(response);
    }
    
    // ========== ENDPOINTS DE VISTA (legacy) ==========

    // Ver todos los usuarios
    @GetMapping("/view/usuarios")
    public ResponseEntity<?> getUsuarios() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Ver todas las carreras
    @GetMapping("/view/carreras")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok(carreraService.getAllCarreras());
    }

    // Ver todas las materias
    @GetMapping("/view/materias")
    public ResponseEntity<?> getMaterias() {
        return ResponseEntity.ok(materiaService.getAllMaterias());
    }

    // Ver todas las habilidades
    @GetMapping("/view/habilidades")
    public ResponseEntity<?> getHabilidades() {
        return ResponseEntity.ok(habilidadService.getAllHabilidades());
    }

    // Ver todas las preguntas del test
    @GetMapping("/view/preguntas")
    public ResponseEntity<?> getPreguntas() {
        return ResponseEntity.ok(preguntaService.getAllPreguntas());
    }

    // Ver todos los estudiantes
    @GetMapping("/view/estudiantes")
    public ResponseEntity<?> getEstudiantes() {
        return ResponseEntity.ok(estudianteService.getAllEstudiantes());
    }

    // Ver estadísticas generales del sistema
    @GetMapping("/view/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalUsuarios", userService.getAllUsers().size());
        response.put("totalCarreras", carreraService.getAllCarreras().size());
        response.put("totalMaterias", materiaService.getAllMaterias().size());
        response.put("totalHabilidades", habilidadService.getAllHabilidades().size());
        response.put("totalPreguntas", preguntaService.getAllPreguntas().size());
        response.put("totalEstudiantes", estudianteService.getAllEstudiantes().size());
        return ResponseEntity.ok(response);
    }

    // Ver todos los datos del sistema
    @GetMapping("/view/todo")
    public ResponseEntity<Map<String, Object>> getTodoElSistema() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("usuarios", userService.getAllUsers());
        response.put("carreras", carreraService.getAllCarreras());
        response.put("materias", materiaService.getAllMaterias());
        response.put("habilidades", habilidadService.getAllHabilidades());
        response.put("preguntas", preguntaService.getAllPreguntas());
        response.put("estudiantes", estudianteService.getAllEstudiantes());
        
        Map<String, Integer> totales = new HashMap<>();
        totales.put("usuarios", userService.getAllUsers().size());
        totales.put("carreras", carreraService.getAllCarreras().size());
        totales.put("materias", materiaService.getAllMaterias().size());
        totales.put("habilidades", habilidadService.getAllHabilidades().size());
        totales.put("preguntas", preguntaService.getAllPreguntas().size());
        totales.put("estudiantes", estudianteService.getAllEstudiantes().size());
        
        response.put("totales", totales);
        
        return ResponseEntity.ok(response);
    }
}
