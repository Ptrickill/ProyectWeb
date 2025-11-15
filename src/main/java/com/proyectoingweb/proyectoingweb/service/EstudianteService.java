package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstudianteService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    // Obtener todos los estudiantes
    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }
    
    // Obtener estudiante por ID
    public Optional<Estudiante> getEstudianteById(Long id) {
        return estudianteRepository.findById(id);
    }
    
    // Crear nuevo estudiante
    public Estudiante createEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }
    
    // Actualizar estudiante
    public Estudiante updateEstudiante(Long id, Estudiante estudianteActualizado) {
        return estudianteRepository.findById(id)
            .map(estudiante -> {
                estudiante.setNombre(estudianteActualizado.getNombre());
                estudiante.setEdad(estudianteActualizado.getEdad());
                return estudianteRepository.save(estudiante);
            })
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
    }
    
    // Eliminar estudiante
    public void deleteEstudiante(Long id) {
        estudianteRepository.deleteById(id);
    }
    
    // Verificar si existe por nombre
    public boolean existsByNombre(String nombre) {
        return estudianteRepository.existsByNombre(nombre);
    }
}
