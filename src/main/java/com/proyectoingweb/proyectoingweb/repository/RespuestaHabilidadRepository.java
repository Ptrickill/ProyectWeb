package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.RespuestaHabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespuestaHabilidadRepository extends JpaRepository<RespuestaHabilidad, Long> {
    
    // Buscar todas las respuestas de un estudiante
    List<RespuestaHabilidad> findByEstudianteId(Long estudianteId);
    
    // Buscar respuesta específica de estudiante a una pregunta
    Optional<RespuestaHabilidad> findByEstudianteIdAndPreguntaId(Long estudianteId, Long preguntaId);
    
    // Verificar si existe respuesta (VALIDACIÓN CRÍTICA - evitar duplicados)
    boolean existsByEstudianteIdAndPreguntaId(Long estudianteId, Long preguntaId);
    
    // Calcular promedio de respuestas de un estudiante
    @Query("SELECT AVG(r.puntaje) FROM RespuestaHabilidad r WHERE r.estudiante.id = :estudianteId")
    Float calcularPromedioRespuestas(@Param("estudianteId") Long estudianteId);
    
    // Obtener respuestas de un estudiante para una habilidad específica
    @Query("SELECT r FROM RespuestaHabilidad r WHERE r.estudiante.id = :estudianteId AND r.pregunta.habilidad.id = :habilidadId")
    List<RespuestaHabilidad> findByEstudianteIdAndHabilidadId(@Param("estudianteId") Long estudianteId, @Param("habilidadId") Long habilidadId);
}
