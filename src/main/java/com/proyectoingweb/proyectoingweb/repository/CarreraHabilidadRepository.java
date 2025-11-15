package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.CarreraHabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraHabilidadRepository extends JpaRepository<CarreraHabilidad, Long> {
    
    // Buscar todos los pesos de habilidades de una carrera (VALIDACIÓN CRÍTICA)
    List<CarreraHabilidad> findByCarreraId(Long carreraId);
    
    // Buscar peso específico de carrera-habilidad
    Optional<CarreraHabilidad> findByCarreraIdAndHabilidadId(Long carreraId, Long habilidadId);
    
    // Verificar si existe peso para carrera-habilidad
    boolean existsByCarreraIdAndHabilidadId(Long carreraId, Long habilidadId);
    
    // Contar pesos configurados para una carrera (VALIDACIÓN CRÍTICA)
    long countByCarreraId(Long carreraId);
}
