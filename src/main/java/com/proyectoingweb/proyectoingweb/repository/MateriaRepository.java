package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    
    // Buscar materia por nombre
    Optional<Materia> findByNombre(String nombre);
    
    // Buscar todas las materias de un área
    List<Materia> findByArea(String area);
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
}
