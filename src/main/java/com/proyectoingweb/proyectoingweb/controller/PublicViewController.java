package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para ver datos del sistema desde el navegador (endpoints GET públicos)
 * Accede a: http://localhost:8080/api/view/...
 */
@RestController
@RequestMapping("/api/view")
@CrossOrigin(origins = "http://localhost:4200")
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

    // Ver todos los usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Ver todas las carreras
    @GetMapping("/carreras")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok(carreraService.getAllCarreras());
    }

    // Ver todas las materias
    @GetMapping("/materias")
    public ResponseEntity<?> getMaterias() {
        return ResponseEntity.ok(materiaService.getAllMaterias());
    }

    // Ver todas las habilidades
    @GetMapping("/habilidades")
    public ResponseEntity<?> getHabilidades() {
        return ResponseEntity.ok(habilidadService.getAllHabilidades());
    }

    // Ver todas las preguntas del test
    @GetMapping("/preguntas")
    public ResponseEntity<?> getPreguntas() {
        return ResponseEntity.ok(preguntaService.getAllPreguntas());
    }

    // Ver todos los estudiantes
    @GetMapping("/estudiantes")
    public ResponseEntity<?> getEstudiantes() {
        return ResponseEntity.ok(estudianteService.getAllEstudiantes());
    }

    // Ver estadísticas generales del sistema
    @GetMapping("/estadisticas")
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
    @GetMapping("/todo")
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
