package com.proyectoingweb.proyectoingweb.strategy;

import com.proyectoingweb.proyectoingweb.entity.CarreraHabilidad;
import com.proyectoingweb.proyectoingweb.entity.RespuestaHabilidad;
import com.proyectoingweb.proyectoingweb.repository.CarreraHabilidadRepository;
import com.proyectoingweb.proyectoingweb.repository.RespuestaHabilidadRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estrategia concreta para cálculo del componente de habilidades.
 * Fórmula: Suma(PromedioHabilidad × PesoHabilidad) / SumaPesos / 5
 */
@Component
public class ComponenteHabilidadesStrategy implements ComponenteCalculoStrategy {
    
    private final RespuestaHabilidadRepository respuestaHabilidadRepository;
    private final CarreraHabilidadRepository carreraHabilidadRepository;
    
    // Inyección por constructor (mejor práctica DIP)
    public ComponenteHabilidadesStrategy(RespuestaHabilidadRepository respuestaHabilidadRepository,
                                        CarreraHabilidadRepository carreraHabilidadRepository) {
        this.respuestaHabilidadRepository = respuestaHabilidadRepository;
        this.carreraHabilidadRepository = carreraHabilidadRepository;
    }
    
    @Override
    public float calcular(Long estudianteId, Long carreraId) {
        // Obtener respuestas del estudiante
        List<RespuestaHabilidad> respuestas = respuestaHabilidadRepository.findByEstudianteId(estudianteId);
        
        if (respuestas.isEmpty()) {
            return 0.0f;
        }
        
        // Obtener pesos de habilidades para esta carrera
        List<CarreraHabilidad> pesosCarrera = carreraHabilidadRepository.findByCarreraId(carreraId);
        
        if (pesosCarrera.isEmpty()) {
            return 0.0f;
        }
        
        // Agrupar respuestas por habilidad y calcular promedio
        Map<Long, List<Integer>> respuestasPorHabilidad = new HashMap<>();
        
        for (RespuestaHabilidad respuesta : respuestas) {
            Long habilidadId = respuesta.getPregunta().getHabilidad().getId();
            respuestasPorHabilidad.putIfAbsent(habilidadId, new ArrayList<>());
            respuestasPorHabilidad.get(habilidadId).add(respuesta.getPuntaje());
        }
        
        // Calcular promedio por habilidad
        Map<Long, Float> promediosPorHabilidad = new HashMap<>();
        
        for (Map.Entry<Long, List<Integer>> entry : respuestasPorHabilidad.entrySet()) {
            List<Integer> puntajes = entry.getValue();
            float promedio = (float) puntajes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            promediosPorHabilidad.put(entry.getKey(), promedio);
        }
        
        // Crear mapa de pesos por habilidad
        Map<Long, Float> pesosPorHabilidad = new HashMap<>();
        float sumaPesos = 0.0f;
        
        for (CarreraHabilidad ch : pesosCarrera) {
            pesosPorHabilidad.put(ch.getHabilidad().getId(), ch.getPeso());
            sumaPesos += ch.getPeso();
        }
        
        // Calcular suma ponderada
        float sumaPonderada = 0.0f;
        
        for (Map.Entry<Long, Float> entry : promediosPorHabilidad.entrySet()) {
            Long habilidadId = entry.getKey();
            if (pesosPorHabilidad.containsKey(habilidadId)) {
                float peso = pesosPorHabilidad.get(habilidadId);
                sumaPonderada += entry.getValue() * peso;
            }
        }
        
        // Normalizar (dividir entre suma de pesos y luego entre 5)
        if (sumaPesos == 0) return 0.0f;
        
        float promedioPonderado = sumaPonderada / sumaPesos;
        float normalizado = promedioPonderado / 5.0f; // Respuestas van de 1-5
        
        return normalizado;
    }
    
    @Override
    public String getNombreComponente() {
        return "Habilidades";
    }
}
