package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Nota;
import com.proyectoingweb.proyectoingweb.repository.EstudianteRepository;
import com.proyectoingweb.proyectoingweb.repository.MateriaRepository;
import com.proyectoingweb.proyectoingweb.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotaService {
    
    @Autowired
    private NotaRepository notaRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    // Obtener todas las notas
    public List<Nota> getAllNotas() {
        return notaRepository.findAll();
    }
    
    // Obtener nota por ID
    public Optional<Nota> getNotaById(Long id) {
        return notaRepository.findById(id);
    }
    
    // Obtener notas de un estudiante
    public List<Nota> getNotasByEstudiante(Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }
    
    // ========== VALIDACIÓN CRÍTICA 4: RANGO DE NOTAS (0-10) ==========
    public Nota createNota(Nota nota) {
        // Validar rango de calificación (0-10)
        if (nota.getCalificacion() < 0 || nota.getCalificacion() > 10) {
            throw new RuntimeException(
                "La calificación debe estar entre 0 y 10. Valor recibido: " + nota.getCalificacion()
            );
        }
        
        // Validar que el estudiante existe
        if (!estudianteRepository.existsById(nota.getEstudiante().getId())) {
            throw new RuntimeException("El estudiante no existe");
        }
        
        // Validar que la materia existe
        if (!materiaRepository.existsById(nota.getMateria().getId())) {
            throw new RuntimeException("La materia no existe");
        }
        
        // Validar que no exista nota duplicada
        if (notaRepository.existsByEstudianteIdAndMateriaId(
            nota.getEstudiante().getId(), nota.getMateria().getId())) {
            throw new RuntimeException(
                "Ya existe una nota para este estudiante en esta materia. Use actualización en su lugar."
            );
        }
        
        return notaRepository.save(nota);
    }
    
    // Actualizar nota
    public Nota updateNota(Long id, Nota notaActualizada) {
        return notaRepository.findById(id)
            .map(nota -> {
                // Validar rango de calificación (0-10)
                if (notaActualizada.getCalificacion() < 0 || notaActualizada.getCalificacion() > 10) {
                    throw new RuntimeException(
                        "La calificación debe estar entre 0 y 10. Valor recibido: " + notaActualizada.getCalificacion()
                    );
                }
                
                nota.setCalificacion(notaActualizada.getCalificacion());
                return notaRepository.save(nota);
            })
            .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));
    }
    
    // Eliminar nota
    public void deleteNota(Long id) {
        notaRepository.deleteById(id);
    }
    
    // Calcular promedio académico de un estudiante
    public Float calcularPromedioAcademico(Long estudianteId) {
        Float promedio = notaRepository.calcularPromedioEstudiante(estudianteId);
        return promedio != null ? promedio : 0.0f;
    }
    
    // Obtener notas por área
    public List<Nota> getNotasByEstudianteAndArea(Long estudianteId, String area) {
        return notaRepository.findByEstudianteIdAndArea(estudianteId, area);
    }
}
