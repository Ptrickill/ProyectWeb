package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Afinidad;
import com.proyectoingweb.proyectoingweb.repository.AfinidadRepository;
import com.proyectoingweb.proyectoingweb.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AfinidadService {
    
    @Autowired
    private AfinidadRepository afinidadRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    // Obtener todas las afinidades
    public List<Afinidad> getAllAfinidades() {
        return afinidadRepository.findAll();
    }
    
    // Obtener afinidad por ID
    public Optional<Afinidad> getAfinidadById(Long id) {
        return afinidadRepository.findById(id);
    }
    
    // Obtener afinidades de un estudiante
    public List<Afinidad> getAfinidadesByEstudiante(Long estudianteId) {
        return afinidadRepository.findByEstudianteId(estudianteId);
    }
    
    // Crear nueva afinidad
    public Afinidad createAfinidad(Afinidad afinidad) {
        // Validar que el estudiante existe
        if (!estudianteRepository.existsById(afinidad.getEstudiante().getId())) {
            throw new RuntimeException("El estudiante no existe");
        }
        
        // Evitar afinidades duplicadas
        if (afinidadRepository.existsByEstudianteIdAndArea(
            afinidad.getEstudiante().getId(), afinidad.getArea())) {
            throw new RuntimeException(
                "El estudiante ya tiene afinidad registrada en esta área"
            );
        }
        
        return afinidadRepository.save(afinidad);
    }
    
    // Actualizar afinidad
    public Afinidad updateAfinidad(Long id, Afinidad afinidadActualizada) {
        return afinidadRepository.findById(id)
            .map(afinidad -> {
                afinidad.setArea(afinidadActualizada.getArea());
                return afinidadRepository.save(afinidad);
            })
            .orElseThrow(() -> new RuntimeException("Afinidad no encontrada con ID: " + id));
    }
    
    // Eliminar afinidad
    public void deleteAfinidad(Long id) {
        afinidadRepository.deleteById(id);
    }
    
    // Contar afinidades de un estudiante
    public long countAfinidadesByEstudiante(Long estudianteId) {
        return afinidadRepository.countByEstudianteId(estudianteId);
    }
}
