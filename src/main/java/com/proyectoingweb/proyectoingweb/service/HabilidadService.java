package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Habilidad;
import com.proyectoingweb.proyectoingweb.entity.Pregunta;
import com.proyectoingweb.proyectoingweb.repository.HabilidadRepository;
import com.proyectoingweb.proyectoingweb.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HabilidadService {
    
    @Autowired
    private HabilidadRepository habilidadRepository;
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    // Obtener todas las habilidades
    public List<Habilidad> getAllHabilidades() {
        return habilidadRepository.findAll();
    }
    
    // Obtener habilidad por ID
    public Optional<Habilidad> getHabilidadById(Long id) {
        return habilidadRepository.findById(id);
    }
    
    // ========== VALIDACIÓN CRÍTICA 3: NOMBRE ÚNICO DE HABILIDAD ==========
    public Habilidad createHabilidad(Habilidad habilidad) {
        // Validar nombre único
        if (habilidadRepository.existsByNombre(habilidad.getNombre())) {
            throw new RuntimeException("Ya existe una habilidad con el nombre: " + habilidad.getNombre());
        }
        
        return habilidadRepository.save(habilidad);
    }
    
    // Actualizar habilidad
    public Habilidad updateHabilidad(Long id, Habilidad habilidadActualizada) {
        return habilidadRepository.findById(id)
            .map(habilidad -> {
                // Validar nombre único si cambió
                if (!habilidad.getNombre().equals(habilidadActualizada.getNombre()) &&
                    habilidadRepository.existsByNombre(habilidadActualizada.getNombre())) {
                    throw new RuntimeException("El nombre ya está en uso: " + habilidadActualizada.getNombre());
                }
                
                habilidad.setNombre(habilidadActualizada.getNombre());
                habilidad.setDescripcion(habilidadActualizada.getDescripcion());
                return habilidadRepository.save(habilidad);
            })
            .orElseThrow(() -> new RuntimeException("Habilidad no encontrada con ID: " + id));
    }
    
    // Eliminar habilidad
    public void deleteHabilidad(Long id) {
        habilidadRepository.deleteById(id);
    }
    
    // ========== VALIDACIÓN: MÍNIMO 2 PREGUNTAS POR HABILIDAD ==========
    public void validarHabilidadActiva(Long habilidadId) {
        List<Pregunta> preguntas = preguntaRepository.findByHabilidadId(habilidadId);
        
        if (preguntas.size() < 2) {
            throw new RuntimeException(
                "La habilidad debe tener al menos 2 preguntas activas (una de gusto y una de autoevaluación)"
            );
        }
        
        // Validar que tenga al menos una pregunta de cada tipo
        boolean tieneGusto = preguntas.stream()
            .anyMatch(p -> "GUSTO".equals(p.getTipoPregunta()));
        boolean tieneAutoevaluacion = preguntas.stream()
            .anyMatch(p -> "AUTOEVALUACION".equals(p.getTipoPregunta()));
        
        if (!tieneGusto || !tieneAutoevaluacion) {
            throw new RuntimeException(
                "La habilidad debe tener al menos una pregunta de gusto y una de autoevaluación"
            );
        }
    }
    
    // Verificar si una habilidad está lista para usarse
    public boolean isHabilidadCompleta(Long habilidadId) {
        try {
            validarHabilidadActiva(habilidadId);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
    
    // Verificar si existe por nombre
    public boolean existsByNombre(String nombre) {
        return habilidadRepository.existsByNombre(nombre);
    }
}
