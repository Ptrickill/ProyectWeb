package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.*;
import com.proyectoingweb.proyectoingweb.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/pesos")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminPesoController {

    @Autowired
    private CarreraMateriaService carreraMateriaService;
    
    @Autowired
    private CarreraHabilidadService carreraHabilidadService;
    
    @Autowired
    private CarreraAfinidadService carreraAfinidadService;
    
    @Autowired
    private CarreraService carreraService;
    
    @Autowired
    private MateriaService materiaService;
    
    @Autowired
    private HabilidadService habilidadService;

    // ==================== PESOS DE MATERIAS ====================
    
    // Obtener pesos de materias de una carrera
    @GetMapping("/carrera-materia/{carreraId}")
    public ResponseEntity<Map<String, Object>> getPesosMateria(@PathVariable Long carreraId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarreraMateria> pesos = carreraMateriaService.getPesosByCarrera(carreraId);
            response.put("success", true);
            response.put("pesos", pesos);
            response.put("total", pesos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener pesos: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear peso materia
    @PostMapping("/carrera-materia")
    public ResponseEntity<Map<String, Object>> createPesoMateria(@RequestBody PesoMateriaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Carrera> carrera = carreraService.getCarreraById(request.getCarreraId());
            Optional<Materia> materia = materiaService.getMateriaById(request.getMateriaId());
            
            if (!carrera.isPresent() || !materia.isPresent()) {
                response.put("success", false);
                response.put("message", "Carrera o materia no encontrada");
                return ResponseEntity.ok(response);
            }
            
            CarreraMateria peso = new CarreraMateria(request.getPeso(), carrera.get(), materia.get());
            CarreraMateria saved = carreraMateriaService.createPeso(peso);
            
            response.put("success", true);
            response.put("message", "Peso asignado exitosamente");
            response.put("peso", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar peso materia
    @PutMapping("/carrera-materia/{id}")
    public ResponseEntity<Map<String, Object>> updatePesoMateria(@PathVariable Long id, @RequestBody PesoUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarreraMateria pesoActualizado = new CarreraMateria();
            pesoActualizado.setPeso(request.getPeso());
            
            CarreraMateria updated = carreraMateriaService.updatePeso(id, pesoActualizado);
            
            response.put("success", true);
            response.put("message", "Peso actualizado exitosamente");
            response.put("peso", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar peso materia
    @DeleteMapping("/carrera-materia/{id}")
    public ResponseEntity<Map<String, Object>> deletePesoMateria(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            carreraMateriaService.deletePeso(id);
            response.put("success", true);
            response.put("message", "Peso eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // ==================== PESOS DE HABILIDADES ====================
    
    // Obtener pesos de habilidades de una carrera
    @GetMapping("/carrera-habilidad/{carreraId}")
    public ResponseEntity<Map<String, Object>> getPesosHabilidad(@PathVariable Long carreraId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarreraHabilidad> pesos = carreraHabilidadService.getPesosByCarrera(carreraId);
            response.put("success", true);
            response.put("pesos", pesos);
            response.put("total", pesos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener pesos: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear peso habilidad
    @PostMapping("/carrera-habilidad")
    public ResponseEntity<Map<String, Object>> createPesoHabilidad(@RequestBody PesoHabilidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Carrera> carrera = carreraService.getCarreraById(request.getCarreraId());
            Optional<Habilidad> habilidad = habilidadService.getHabilidadById(request.getHabilidadId());
            
            if (!carrera.isPresent() || !habilidad.isPresent()) {
                response.put("success", false);
                response.put("message", "Carrera o habilidad no encontrada");
                return ResponseEntity.ok(response);
            }
            
            CarreraHabilidad peso = new CarreraHabilidad(request.getPeso(), carrera.get(), habilidad.get());
            CarreraHabilidad saved = carreraHabilidadService.createPeso(peso);
            
            response.put("success", true);
            response.put("message", "Peso asignado exitosamente");
            response.put("peso", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar peso habilidad
    @PutMapping("/carrera-habilidad/{id}")
    public ResponseEntity<Map<String, Object>> updatePesoHabilidad(@PathVariable Long id, @RequestBody PesoUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarreraHabilidad pesoActualizado = new CarreraHabilidad();
            pesoActualizado.setPeso(request.getPeso());
            
            CarreraHabilidad updated = carreraHabilidadService.updatePeso(id, pesoActualizado);
            
            response.put("success", true);
            response.put("message", "Peso actualizado exitosamente");
            response.put("peso", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar peso habilidad
    @DeleteMapping("/carrera-habilidad/{id}")
    public ResponseEntity<Map<String, Object>> deletePesoHabilidad(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            carreraHabilidadService.deletePeso(id);
            response.put("success", true);
            response.put("message", "Peso eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // ==================== PESOS DE AFINIDADES ====================
    
    // Obtener pesos de afinidades de una carrera
    @GetMapping("/carrera-afinidad/{carreraId}")
    public ResponseEntity<Map<String, Object>> getPesosAfinidad(@PathVariable Long carreraId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarreraAfinidad> pesos = carreraAfinidadService.getPesosByCarrera(carreraId);
            response.put("success", true);
            response.put("pesos", pesos);
            response.put("total", pesos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener pesos: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Crear peso afinidad
    @PostMapping("/carrera-afinidad")
    public ResponseEntity<Map<String, Object>> createPesoAfinidad(@RequestBody PesoAfinidadRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Carrera> carrera = carreraService.getCarreraById(request.getCarreraId());
            
            if (!carrera.isPresent()) {
                response.put("success", false);
                response.put("message", "Carrera no encontrada");
                return ResponseEntity.ok(response);
            }
            
            CarreraAfinidad peso = new CarreraAfinidad(request.getArea(), request.getPeso(), carrera.get());
            CarreraAfinidad saved = carreraAfinidadService.createPeso(peso);
            
            response.put("success", true);
            response.put("message", "Peso asignado exitosamente");
            response.put("peso", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar peso afinidad
    @PutMapping("/carrera-afinidad/{id}")
    public ResponseEntity<Map<String, Object>> updatePesoAfinidad(@PathVariable Long id, @RequestBody PesoAfinidadUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarreraAfinidad pesoActualizado = new CarreraAfinidad();
            pesoActualizado.setPeso(request.getPeso());
            pesoActualizado.setArea(request.getArea());
            
            CarreraAfinidad updated = carreraAfinidadService.updatePeso(id, pesoActualizado);
            
            response.put("success", true);
            response.put("message", "Peso actualizado exitosamente");
            response.put("peso", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Eliminar peso afinidad
    @DeleteMapping("/carrera-afinidad/{id}")
    public ResponseEntity<Map<String, Object>> deletePesoAfinidad(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            carreraAfinidadService.deletePeso(id);
            response.put("success", true);
            response.put("message", "Peso eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar peso: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // ==================== OBTENER TODOS LOS PESOS DE UNA CARRERA ====================
    
    @GetMapping("/carrera/{carreraId}")
    public ResponseEntity<Map<String, Object>> getAllPesosCarrera(@PathVariable Long carreraId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarreraMateria> pesosMaterias = carreraMateriaService.getPesosByCarrera(carreraId);
            List<CarreraHabilidad> pesosHabilidades = carreraHabilidadService.getPesosByCarrera(carreraId);
            List<CarreraAfinidad> pesosAfinidades = carreraAfinidadService.getPesosByCarrera(carreraId);
            
            response.put("success", true);
            response.put("pesosMaterias", pesosMaterias);
            response.put("pesosHabilidades", pesosHabilidades);
            response.put("pesosAfinidades", pesosAfinidades);
            response.put("totalMaterias", pesosMaterias.size());
            response.put("totalHabilidades", pesosHabilidades.size());
            response.put("totalAfinidades", pesosAfinidades.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener pesos: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // ==================== CLASES PARA REQUESTS ====================
    
    public static class PesoMateriaRequest {
        private Long carreraId;
        private Long materiaId;
        private Float peso;

        public Long getCarreraId() { return carreraId; }
        public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
        public Long getMateriaId() { return materiaId; }
        public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }
        public Float getPeso() { return peso; }
        public void setPeso(Float peso) { this.peso = peso; }
    }

    public static class PesoHabilidadRequest {
        private Long carreraId;
        private Long habilidadId;
        private Float peso;

        public Long getCarreraId() { return carreraId; }
        public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
        public Long getHabilidadId() { return habilidadId; }
        public void setHabilidadId(Long habilidadId) { this.habilidadId = habilidadId; }
        public Float getPeso() { return peso; }
        public void setPeso(Float peso) { this.peso = peso; }
    }

    public static class PesoAfinidadRequest {
        private Long carreraId;
        private String area;
        private Float peso;

        public Long getCarreraId() { return carreraId; }
        public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
        public Float getPeso() { return peso; }
        public void setPeso(Float peso) { this.peso = peso; }
    }

    public static class PesoUpdateRequest {
        private Float peso;

        public Float getPeso() { return peso; }
        public void setPeso(Float peso) { this.peso = peso; }
    }

    public static class PesoAfinidadUpdateRequest {
        private String area;
        private Float peso;

        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
        public Float getPeso() { return peso; }
        public void setPeso(Float peso) { this.peso = peso; }
    }
}
