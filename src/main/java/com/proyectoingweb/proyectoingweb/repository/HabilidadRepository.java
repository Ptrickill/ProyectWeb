package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Habilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabilidadRepository extends JpaRepository<Habilidad, Long> {
    
    // Buscar habilidad por nombre (VALIDACIÓN CRÍTICA)
    Optional<Habilidad> findByNombre(String nombre);
    
    // Verificar si existe por nombre (VALIDACIÓN CRÍTICA)
    boolean existsByNombre(String nombre);
}
