package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Afinidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AfinidadRepository extends JpaRepository<Afinidad, Long> {
    
    // Buscar todas las afinidades de un estudiante
    List<Afinidad> findByEstudianteId(Long estudianteId);
    
    // Buscar afinidad específica de estudiante por área
    Optional<Afinidad> findByEstudianteIdAndArea(Long estudianteId, String area);
    
    // Verificar si existe afinidad de estudiante en área
    boolean existsByEstudianteIdAndArea(Long estudianteId, String area);
    
    // Contar afinidades de un estudiante
    long countByEstudianteId(Long estudianteId);
}
