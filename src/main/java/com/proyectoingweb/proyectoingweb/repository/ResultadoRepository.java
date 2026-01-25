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
    
    // Reporte: Carreras más recomendadas (las que más aparecen en los resultados)
    @Query(value = "SELECT c.nombre, COUNT(DISTINCT r.estudiante_id) as total_estudiantes " +
                   "FROM resultados r " +
                   "JOIN carreras c ON r.carrera_id = c.id " +
                   "GROUP BY c.id, c.nombre " +
                   "ORDER BY total_estudiantes DESC " +
                   "LIMIT 10", 
           nativeQuery = true)
    List<Object[]> findCarrerasRecomendadas();
}
