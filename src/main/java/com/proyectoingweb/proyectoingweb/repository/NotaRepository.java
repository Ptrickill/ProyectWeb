package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    
    // Buscar todas las notas de un estudiante
    List<Nota> findByEstudianteId(Long estudianteId);
    
    // Buscar nota específica de estudiante en una materia
    Optional<Nota> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
    
    // Verificar si existe nota de estudiante en materia
    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
    
    // Calcular promedio de notas de un estudiante
    @Query("SELECT AVG(n.calificacion) FROM Nota n WHERE n.estudiante.id = :estudianteId")
    Float calcularPromedioEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Obtener notas de un estudiante por área
    @Query("SELECT n FROM Nota n WHERE n.estudiante.id = :estudianteId AND n.materia.area = :area")
    List<Nota> findByEstudianteIdAndArea(@Param("estudianteId") Long estudianteId, @Param("area") String area);
}
