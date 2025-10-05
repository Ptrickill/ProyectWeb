package com.proyectoingweb.proyectoingweb.repository;

import com.proyectoingweb.proyectoingweb.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Métodos automáticos que ya vienen con JpaRepository:
    // save(Usuario) - guardar
    // findById(Long) - buscar por ID
    // findAll() - buscar todos
    // deleteById(Long) - eliminar por ID
    // count() - contar registros
    
    // Métodos personalizados
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByActivoTrue();
    
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombre")
    List<Usuario> findUsuariosActivosOrdenadosPorNombre();
}