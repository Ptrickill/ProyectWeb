package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.RespuestaHabilidad;
import com.proyectoingweb.proyectoingweb.repository.EstudianteRepository;
import com.proyectoingweb.proyectoingweb.repository.PreguntaRepository;
import com.proyectoingweb.proyectoingweb.repository.RespuestaHabilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RespuestaHabilidadService {
    
    @Autowired
    private RespuestaHabilidadRepository respuestaHabilidadRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    // Obtener todas las respuestas
    public List<RespuestaHabilidad> getAllRespuestas() {
        return respuestaHabilidadRepository.findAll();
    }
    
    // Obtener respuesta por ID
    public Optional<RespuestaHabilidad> getRespuestaById(Long id) {
        return respuestaHabilidadRepository.findById(id);
    }
    
    // Obtener respuestas de un estudiante
    public List<RespuestaHabilidad> getRespuestasByEstudiante(Long estudianteId) {
        return respuestaHabilidadRepository.findByEstudianteId(estudianteId);
    }
    
    // ========== VALIDACIÓN CRÍTICA 5: RANGO DE RESPUESTAS (1-5) ==========
    public RespuestaHabilidad createRespuesta(RespuestaHabilidad respuesta) {
        // Validar rango de puntaje (1-5)
        if (respuesta.getPuntaje() < 1 || respuesta.getPuntaje() > 5) {
            throw new RuntimeException(
                "El puntaje de la respuesta debe estar entre 1 y 5. Valor recibido: " + respuesta.getPuntaje()
            );
        }
        
        // Validar que el estudiante existe
        if (!estudianteRepository.existsById(respuesta.getEstudiante().getId())) {
            throw new RuntimeException("El estudiante no existe");
        }
        
        // Validar que la pregunta existe
        if (!preguntaRepository.existsById(respuesta.getPregunta().getId())) {
            throw new RuntimeException("La pregunta no existe");
        }
        
        // Evitar respuestas duplicadas
        if (respuestaHabilidadRepository.existsByEstudianteIdAndPreguntaId(
            respuesta.getEstudiante().getId(), respuesta.getPregunta().getId())) {
            throw new RuntimeException(
                "El estudiante ya respondió esta pregunta. Use actualización en su lugar."
            );
        }
        
        return respuestaHabilidadRepository.save(respuesta);
    }
    
    // Actualizar respuesta
    public RespuestaHabilidad updateRespuesta(Long id, RespuestaHabilidad respuestaActualizada) {
        return respuestaHabilidadRepository.findById(id)
            .map(respuesta -> {
                // Validar rango de puntaje (1-5)
                if (respuestaActualizada.getPuntaje() < 1 || respuestaActualizada.getPuntaje() > 5) {
                    throw new RuntimeException(
                        "El puntaje debe estar entre 1 y 5. Valor recibido: " + respuestaActualizada.getPuntaje()
                    );
                }
                
                respuesta.setPuntaje(respuestaActualizada.getPuntaje());
                return respuestaHabilidadRepository.save(respuesta);
            })
            .orElseThrow(() -> new RuntimeException("Respuesta no encontrada con ID: " + id));
    }
    
    // Eliminar respuesta
    public void deleteRespuesta(Long id) {
        respuestaHabilidadRepository.deleteById(id);
    }
    
    // Calcular promedio de habilidades de un estudiante
    public Float calcularPromedioHabilidades(Long estudianteId) {
        Float promedio = respuestaHabilidadRepository.calcularPromedioRespuestas(estudianteId);
        return promedio != null ? promedio : 0.0f;
    }
    
    // Obtener respuestas por habilidad
    public List<RespuestaHabilidad> getRespuestasByEstudianteAndHabilidad(Long estudianteId, Long habilidadId) {
        return respuestaHabilidadRepository.findByEstudianteIdAndHabilidadId(estudianteId, habilidadId);
    }
}
