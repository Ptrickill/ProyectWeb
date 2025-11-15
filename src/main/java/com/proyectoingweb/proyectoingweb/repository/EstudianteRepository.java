package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Buscar estudiante por nombre
    Optional<Estudiante> findByNombre(String nombre);
    
    // Verificar si existe un estudiante por nombre
    boolean existsByNombre(String nombre);
}
