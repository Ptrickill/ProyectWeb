package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    
    // Buscar todos los resultados de un estudiante
    List<Resultado> findByEstudianteId(Long estudianteId);
    
    // Buscar resultados de un estudiante ordenados por puntaje (TOP 3)
    List<Resultado> findByEstudianteIdOrderByPuntajeFinalDesc(Long estudianteId);
    
    // Obtener Top N resultados de un estudiante
    @Query("SELECT r FROM Resultado r WHERE r.estudiante.id = :estudianteId ORDER BY r.puntajeFinal DESC")
    List<Resultado> findTopNByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    // Buscar resultado de un estudiante para una carrera específica
    List<Resultado> findByEstudianteIdAndCarreraId(Long estudianteId, Long carreraId);
    
    // Eliminar todos los resultados de un estudiante (para recalcular)
    void deleteByEstudianteId(Long estudianteId);
    
    // Reporte: Carreras más recomendadas (solo las del Top 3 de cada estudiante)
    @Query(value = "WITH ranked_resultados AS ( " +
                   "  SELECT r.estudiante_id, r.carrera_id, c.nombre, " +
                   "         ROW_NUMBER() OVER (PARTITION BY r.estudiante_id ORDER BY r.puntaje_final DESC) as ranking " +
                   "  FROM resultados r " +
                   "  JOIN carreras c ON r.carrera_id = c.id " +
                   ") " +
                   "SELECT nombre, COUNT(DISTINCT estudiante_id) as total_estudiantes " +
                   "FROM ranked_resultados " +
                   "WHERE ranking <= 3 " +
                   "GROUP BY nombre " +
                   "ORDER BY total_estudiantes DESC " +
                   "LIMIT 10", 
           nativeQuery = true)
    List<Object[]> findCarrerasRecomendadas();
}
