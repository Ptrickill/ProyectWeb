package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    
    // Buscar carrera por código (VALIDACIÓN CRÍTICA)
    Optional<Carrera> findByCodigo(String codigo);
    
    // Buscar carrera por nombre
    Optional<Carrera> findByNombre(String nombre);
    
    // Verificar si existe por código (VALIDACIÓN CRÍTICA)
    boolean existsByCodigo(String codigo);
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
}
