package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.CarreraAfinidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraAfinidadRepository extends JpaRepository<CarreraAfinidad, Long> {
    
    // Buscar todos los pesos de afinidades de una carrera (VALIDACIÓN CRÍTICA)
    List<CarreraAfinidad> findByCarreraId(Long carreraId);
    
    // Buscar peso específico de carrera-afinidad por área
    Optional<CarreraAfinidad> findByCarreraIdAndArea(Long carreraId, String area);
    
    // Verificar si existe peso para carrera-área
    boolean existsByCarreraIdAndArea(Long carreraId, String area);
    
    // Contar pesos configurados para una carrera (VALIDACIÓN CRÍTICA)
    long countByCarreraId(Long carreraId);
}
