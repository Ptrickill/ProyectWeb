package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Pregunta;
import com.proyectoingweb.proyectoingweb.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PreguntaService {
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    // Obtener todas las preguntas
    public List<Pregunta> getAllPreguntas() {
        return preguntaRepository.findAll();
    }
    
    // Obtener pregunta por ID
    public Optional<Pregunta> getPreguntaById(Long id) {
        return preguntaRepository.findById(id);
    }
    
    // Obtener preguntas por habilidad
    public List<Pregunta> getPreguntasByHabilidad(Long habilidadId) {
        return preguntaRepository.findByHabilidadId(habilidadId);
    }
    
    // Obtener preguntas por tipo
    public List<Pregunta> getPreguntasByTipo(String tipoPregunta) {
        return preguntaRepository.findByTipoPregunta(tipoPregunta);
    }
    
    // Crear nueva pregunta
    public Pregunta createPregunta(Pregunta pregunta) {
        // Validar tipo de pregunta
        if (!"GUSTO".equals(pregunta.getTipoPregunta()) && 
            !"AUTOEVALUACION".equals(pregunta.getTipoPregunta())) {
            throw new RuntimeException(
                "El tipo de pregunta debe ser GUSTO o AUTOEVALUACION"
            );
        }
        
        return preguntaRepository.save(pregunta);
    }
    
    // Actualizar pregunta
    public Pregunta updatePregunta(Long id, Pregunta preguntaActualizada) {
        return preguntaRepository.findById(id)
            .map(pregunta -> {
                // Validar tipo de pregunta
                if (!"GUSTO".equals(preguntaActualizada.getTipoPregunta()) && 
                    !"AUTOEVALUACION".equals(preguntaActualizada.getTipoPregunta())) {
                    throw new RuntimeException(
                        "El tipo de pregunta debe ser GUSTO o AUTOEVALUACION"
                    );
                }
                
                pregunta.setTexto(preguntaActualizada.getTexto());
                pregunta.setTipoPregunta(preguntaActualizada.getTipoPregunta());
                pregunta.setHabilidad(preguntaActualizada.getHabilidad());
                return preguntaRepository.save(pregunta);
            })
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con ID: " + id));
    }
    
    // Eliminar pregunta
    public void deletePregunta(Long id) {
        preguntaRepository.deleteById(id);
    }
}
