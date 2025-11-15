package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.CarreraMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraMateriaRepository extends JpaRepository<CarreraMateria, Long> {
    
    // Buscar todos los pesos de materias de una carrera (VALIDACIÓN CRÍTICA)
    List<CarreraMateria> findByCarreraId(Long carreraId);
    
    // Buscar peso específico de carrera-materia
    Optional<CarreraMateria> findByCarreraIdAndMateriaId(Long carreraId, Long materiaId);
    
    // Verificar si existe peso para carrera-materia
    boolean existsByCarreraIdAndMateriaId(Long carreraId, Long materiaId);
    
    // Contar pesos configurados para una carrera (VALIDACIÓN CRÍTICA)
    long countByCarreraId(Long carreraId);
}
