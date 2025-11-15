package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    
    // Buscar todas las preguntas de una habilidad (VALIDACIÓN CRÍTICA)
    List<Pregunta> findByHabilidadId(Long habilidadId);
    
    // Buscar preguntas por tipo (GUSTO o AUTOEVALUACION)
    List<Pregunta> findByTipoPregunta(String tipoPregunta);
    
    // Buscar preguntas de una habilidad por tipo
    List<Pregunta> findByHabilidadIdAndTipoPregunta(Long habilidadId, String tipoPregunta);
    
    // Contar preguntas de una habilidad (VALIDACIÓN CRÍTICA - mínimo 2)
    long countByHabilidadId(Long habilidadId);
}
