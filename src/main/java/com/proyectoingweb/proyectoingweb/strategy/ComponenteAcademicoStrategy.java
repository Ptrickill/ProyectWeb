package com.proyectoingweb.proyectoingweb.strategy;

import com.proyectoingweb.proyectoingweb.entity.CarreraMateria;
import com.proyectoingweb.proyectoingweb.entity.Nota;
import com.proyectoingweb.proyectoingweb.repository.CarreraMateriaRepository;
import com.proyectoingweb.proyectoingweb.repository.NotaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estrategia concreta para cálculo del componente académico.
 * Fórmula: Suma(Nota × PesoMateria) / SumaPesos / 10
 */
@Component
public class ComponenteAcademicoStrategy implements ComponenteCalculoStrategy {
    
    private final NotaRepository notaRepository;
    private final CarreraMateriaRepository carreraMateriaRepository;
    
    // Inyección por constructor (mejor práctica DIP)
    public ComponenteAcademicoStrategy(NotaRepository notaRepository, 
                                      CarreraMateriaRepository carreraMateriaRepository) {
        this.notaRepository = notaRepository;
        this.carreraMateriaRepository = carreraMateriaRepository;
    }
    
    @Override
    public float calcular(Long estudianteId, Long carreraId) {
        // Obtener notas del estudiante
        List<Nota> notas = notaRepository.findByEstudianteId(estudianteId);
        
        if (notas.isEmpty()) {
            return 0.0f;
        }
        
        // Obtener pesos de materias para esta carrera
        List<CarreraMateria> pesosCarrera = carreraMateriaRepository.findByCarreraId(carreraId);
        
        if (pesosCarrera.isEmpty()) {
            return 0.0f;
        }
        
        // Crear mapa de pesos por materia para búsqueda rápida
        Map<Long, Float> pesosPorMateria = new HashMap<>();
        float sumaPesos = 0.0f;
        
        for (CarreraMateria cm : pesosCarrera) {
            pesosPorMateria.put(cm.getMateria().getId(), cm.getPeso());
            sumaPesos += cm.getPeso();
        }
        
        // Calcular suma ponderada
        float sumaPonderada = 0.0f;
        
        for (Nota nota : notas) {
            Long materiaId = nota.getMateria().getId();
            if (pesosPorMateria.containsKey(materiaId)) {
                float peso = pesosPorMateria.get(materiaId);
                sumaPonderada += nota.getCalificacion() * peso;
            }
        }
        
        // Normalizar (dividir entre suma de pesos y luego entre 10)
        if (sumaPesos == 0) return 0.0f;
        
        float promedioPonderado = sumaPonderada / sumaPesos;
        float normalizado = promedioPonderado / 10.0f; // Notas van de 0-10
        
        return normalizado;
    }
    
    @Override
    public String getNombreComponente() {
        return "Académico";
    }
}
