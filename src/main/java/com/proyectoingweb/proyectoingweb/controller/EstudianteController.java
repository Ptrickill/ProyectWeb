package com.proyectoingweb.proyectoingweb.controller;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;
    
    // Obtener estudiante por usuario ID
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Estudiante> estudiante = estudianteService.getEstudianteByUsuarioId(usuarioId);
            if (estudiante.isPresent()) {
                response.put("success", true);
                response.put("data", estudiante.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Crear perfil de estudiante asociado a un usuario
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> crearPerfilPorUsuario(
            @PathVariable Long usuarioId,
            @RequestBody EstudianteRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre(request.getNombre());
            estudiante.setApellido(request.getApellido());
            estudiante.setEmail(request.getEmail());
            estudiante.setTelefono(request.getTelefono());
            estudiante.setDireccion(request.getDireccion());
            estudiante.setFechaNacimiento(request.getFechaNacimiento());
            estudiante.setUsuarioId(usuarioId);
            
            // Calcular edad desde fecha de nacimiento
            if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
                Integer edad = calcularEdad(request.getFechaNacimiento());
                estudiante.setEdad(edad);
            } else {
                estudiante.setEdad(18); // Edad por defecto
            }
            
            Estudiante saved = estudianteService.createEstudiante(estudiante);
            
            response.put("success", true);
            response.put("message", "Perfil creado exitosamente");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear perfil: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Actualizar perfil de estudiante
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEstudiante(
            @PathVariable Long id,
            @RequestBody EstudianteRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudianteActualizado = new Estudiante();
            estudianteActualizado.setNombre(request.getNombre());
            estudianteActualizado.setApellido(request.getApellido());
            estudianteActualizado.setEmail(request.getEmail());
            estudianteActualizado.setTelefono(request.getTelefono());
            estudianteActualizado.setDireccion(request.getDireccion());
            estudianteActualizado.setFechaNacimiento(request.getFechaNacimiento());
            
            // Calcular edad desde fecha de nacimiento
            if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
                Integer edad = calcularEdad(request.getFechaNacimiento());
                estudianteActualizado.setEdad(edad);
            }
            
            Estudiante updated = estudianteService.updateEstudiante(id, estudianteActualizado);
            
            response.put("success", true);
            response.put("message", "Perfil actualizado exitosamente");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar perfil: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Método auxiliar para calcular edad desde fecha de nacimiento
    private Integer calcularEdad(String fechaNacimiento) {
        try {
            // Formato esperado: "dd/MM/yyyy" o "yyyy-MM-dd"
            String[] partes;
            int año, mes, dia;
            
            if (fechaNacimiento.contains("/")) {
                partes = fechaNacimiento.split("/");
                dia = Integer.parseInt(partes[0]);
                mes = Integer.parseInt(partes[1]);
                año = Integer.parseInt(partes[2]);
            } else if (fechaNacimiento.contains("-")) {
                partes = fechaNacimiento.split("-");
                año = Integer.parseInt(partes[0]);
                mes = Integer.parseInt(partes[1]);
                dia = Integer.parseInt(partes[2]);
            } else {
                return 18; // Edad por defecto si el formato es incorrecto
            }
            
            // Calcular edad actual
            int añoActual = java.time.Year.now().getValue();
            int mesActual = java.time.LocalDate.now().getMonthValue();
            int diaActual = java.time.LocalDate.now().getDayOfMonth();
            
            int edad = añoActual - año;
            
            // Ajustar si aún no ha cumplido años este año
            if (mesActual < mes || (mesActual == mes && diaActual < dia)) {
                edad--;
            }
            
            return edad;
        } catch (Exception e) {
            return 18; // Edad por defecto en caso de error
        }
    }

    // Crear perfil de estudiante
    @PostMapping("/perfil")
    public ResponseEntity<Map<String, Object>> crearPerfil(@RequestBody PerfilRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudiante = new Estudiante(request.getNombre(), request.getEdad());
            Estudiante saved = estudianteService.createEstudiante(estudiante);
            
            response.put("success", true);
            response.put("message", "Perfil creado exitosamente");
            response.put("estudiante", saved);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener perfil por ID
    @GetMapping("/perfil/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPerfil(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Estudiante> estudiante = estudianteService.getEstudianteById(id);
            if (estudiante.isPresent()) {
                response.put("success", true);
                response.put("estudiante", estudiante.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Estudiante no encontrado");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Actualizar perfil
    @PutMapping("/perfil/{id}")
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @PathVariable Long id, 
            @RequestBody PerfilRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Estudiante estudianteActualizado = new Estudiante(request.getNombre(), request.getEdad());
            Estudiante updated = estudianteService.updateEstudiante(id, estudianteActualizado);
            
            response.put("success", true);
            response.put("message", "Perfil actualizado exitosamente");
            response.put("estudiante", updated);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar perfil: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Obtener todos los estudiantes (para admin)
    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Estudiante> estudiantes = estudianteService.getAllEstudiantes();
            response.put("success", true);
            response.put("estudiantes", estudiantes);
            response.put("total", estudiantes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estudiantes: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Clases para request
    public static class PerfilRequest {
        private String nombre;
        private Integer edad;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
    }
    
    public static class EstudianteRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        private String direccion;
        private String fechaNacimiento;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    }
}
