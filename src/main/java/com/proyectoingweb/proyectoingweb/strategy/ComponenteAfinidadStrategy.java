package com.proyectoingweb.proyectoingweb.strategy;

import com.proyectoingweb.proyectoingweb.entity.Afinidad;
import com.proyectoingweb.proyectoingweb.repository.AfinidadRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Estrategia concreta para cálculo del componente de afinidad.
 * Fórmula: Nivel de interés del estudiante en la carrera / 5
 */
@Component
public class ComponenteAfinidadStrategy implements ComponenteCalculoStrategy {
    
    private final AfinidadRepository afinidadRepository;
    
    // Inyección por constructor (mejor práctica DIP)
    public ComponenteAfinidadStrategy(AfinidadRepository afinidadRepository) {
        this.afinidadRepository = afinidadRepository;
    }
    
    @Override
    public float calcular(Long estudianteId, Long carreraId) {
        // Obtener afinidades del estudiante
        List<Afinidad> afinidades = afinidadRepository.findByEstudianteId(estudianteId);
        
        if (afinidades.isEmpty()) {
            return 0.0f;
        }
        
        // Buscar si el estudiante tiene afinidad con esta carrera específica
        Optional<Afinidad> afinidadCarrera = afinidades.stream()
            .filter(a -> a.getCarrera() != null && a.getCarrera().getId().equals(carreraId))
            .findFirst();
        
        if (afinidadCarrera.isPresent()) {
            // Si tiene afinidad directa con esta carrera, usar su nivel de interés
            Integer nivelInteres = afinidadCarrera.get().getNivelInteres();
            if (nivelInteres == null) return 0.0f;
            
            // Normalizar: nivel va de 1-5, convertir a 0-1
            return nivelInteres / 5.0f;
        }
        
        // Si no tiene afinidad registrada para esta carrera, retornar 0
        return 0.0f;
    }
    
    @Override
    public String getNombreComponente() {
        return "Afinidad";
    }
}
