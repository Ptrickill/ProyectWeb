package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Materia;
import com.proyectoingweb.proyectoingweb.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MateriaService {
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    // Obtener todas las materias
    public List<Materia> getAllMaterias() {
        return materiaRepository.findAll();
    }
    
    // Obtener materia por ID
    public Optional<Materia> getMateriaById(Long id) {
        return materiaRepository.findById(id);
    }
    
    // Obtener materias por área
    public List<Materia> getMateriasByArea(String area) {
        return materiaRepository.findByArea(area);
    }
    
    // Crear nueva materia
    public Materia createMateria(Materia materia) {
        // Validar nombre único
        if (materiaRepository.existsByNombre(materia.getNombre())) {
            throw new RuntimeException("Ya existe una materia con el nombre: " + materia.getNombre());
        }
        
        return materiaRepository.save(materia);
    }
    
    // Actualizar materia
    public Materia updateMateria(Long id, Materia materiaActualizada) {
        return materiaRepository.findById(id)
            .map(materia -> {
                // Validar nombre único si cambió
                if (!materia.getNombre().equals(materiaActualizada.getNombre()) &&
                    materiaRepository.existsByNombre(materiaActualizada.getNombre())) {
                    throw new RuntimeException("El nombre ya está en uso: " + materiaActualizada.getNombre());
                }
                
                materia.setNombre(materiaActualizada.getNombre());
                materia.setArea(materiaActualizada.getArea());
                return materiaRepository.save(materia);
            })
            .orElseThrow(() -> new RuntimeException("Materia no encontrada con ID: " + id));
    }
    
    // Eliminar materia
    public void deleteMateria(Long id) {
        materiaRepository.deleteById(id);
    }
    
    // Verificar si existe por nombre
    public boolean existsByNombre(String nombre) {
        return materiaRepository.existsByNombre(nombre);
    }
}
