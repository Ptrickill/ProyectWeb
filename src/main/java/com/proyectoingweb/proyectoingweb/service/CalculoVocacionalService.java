package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.*;
import com.proyectoingweb.proyectoingweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para el cálculo vocacional del CORE del sistema.
 * Implementa la fórmula: Score = 0.5×Académico + 0.3×Habilidades + 0.2×Afinidad
 */
@Service
@Transactional
public class CalculoVocacionalService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private NotaRepository notaRepository;
    
    @Autowired
    private RespuestaHabilidadRepository respuestaHabilidadRepository;
    
    @Autowired
    private AfinidadRepository afinidadRepository;
    
    @Autowired
    private CarreraMateriaRepository carreraMateriaRepository;
    
    @Autowired
    private CarreraHabilidadRepository carreraHabilidadRepository;
    
    @Autowired
    private CarreraAfinidadRepository carreraAfinidadRepository;
    
    @Autowired
    private ResultadoRepository resultadoRepository;
    
    // Constantes de la fórmula
    private static final float PESO_ACADEMICO = 0.5f;
    private static final float PESO_HABILIDADES = 0.3f;
    private static final float PESO_AFINIDAD = 0.2f;
    
    /**
     * PASO 1: Calcular componente académico (50%)
     * Fórmula: Suma(Nota × PesoMateria) / SumaPesos / 10
     */
    public float calcularComponenteAcademico(Long estudianteId, Long carreraId) {
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
    
    /**
     * PASO 2: Calcular componente habilidades (30%)
     * Fórmula: Suma(PromedioHabilidad × PesoHabilidad) / SumaPesos / 5
     */
    public float calcularComponenteHabilidades(Long estudianteId, Long carreraId) {
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
    
    /**
     * PASO 3: Calcular componente afinidad (20%)
     * Fórmula: Suma(PesosÁreasSeleccionadas) / SumaPesosTotales
     */
    public float calcularComponenteAfinidad(Long estudianteId, Long carreraId) {
        // Obtener afinidades del estudiante
        List<Afinidad> afinidades = afinidadRepository.findByEstudianteId(estudianteId);
        
        if (afinidades.isEmpty()) {
            return 0.0f;
        }
        
        // Obtener pesos de afinidades para esta carrera
        List<CarreraAfinidad> pesosCarrera = carreraAfinidadRepository.findByCarreraId(carreraId);
        
        if (pesosCarrera.isEmpty()) {
            return 0.0f;
        }
        
        // Crear mapa de pesos por área
        Map<String, Float> pesosPorArea = new HashMap<>();
        float sumaPesosTotales = 0.0f;
        
        for (CarreraAfinidad ca : pesosCarrera) {
            pesosPorArea.put(ca.getArea(), ca.getPeso());
            sumaPesosTotales += ca.getPeso();
        }
        
        // Sumar pesos de áreas seleccionadas por el estudiante
        float sumaPesosSeleccionados = 0.0f;
        
        for (Afinidad afinidad : afinidades) {
            if (pesosPorArea.containsKey(afinidad.getArea())) {
                sumaPesosSeleccionados += pesosPorArea.get(afinidad.getArea());
            }
        }
        
        // Normalizar
        if (sumaPesosTotales == 0) return 0.0f;
        
        float normalizado = sumaPesosSeleccionados / sumaPesosTotales;
        
        return normalizado;
    }
    
    /**
     * PASO 4: Calcular puntaje final para una carrera
     * Aplica la fórmula: Score = 0.5×Académico + 0.3×Habilidades + 0.2×Afinidad
     */
    public Resultado calcularPuntajeCarrera(Long estudianteId, Long carreraId) {
        // Validar que el estudiante existe
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + estudianteId));
        
        // Validar que la carrera existe
        Carrera carrera = carreraRepository.findById(carreraId)
            .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + carreraId));
        
        // Calcular componentes
        float puntajeAcademico = calcularComponenteAcademico(estudianteId, carreraId);
        float puntajeHabilidades = calcularComponenteHabilidades(estudianteId, carreraId);
        float puntajeAfinidad = calcularComponenteAfinidad(estudianteId, carreraId);
        
        // Aplicar fórmula final
        float puntajeFinal = (PESO_ACADEMICO * puntajeAcademico) + 
                            (PESO_HABILIDADES * puntajeHabilidades) + 
                            (PESO_AFINIDAD * puntajeAfinidad);
        
        // Crear resultado
        Resultado resultado = new Resultado();
        resultado.setEstudiante(estudiante);
        resultado.setCarrera(carrera);
        resultado.setPuntajeFinal(puntajeFinal);
        resultado.setPuntajeAcademico(puntajeAcademico);
        resultado.setPuntajeHabilidades(puntajeHabilidades);
        resultado.setPuntajeAfinidad(puntajeAfinidad);
        
        return resultado;
    }
    
    /**
     * PASO 5: Calcular y guardar resultados para TODAS las carreras
     */
    public List<Resultado> calcularYGuardarResultados(Long estudianteId) {
        // Validaciones previas
        validarDatosEstudiante(estudianteId);
        
        // Eliminar resultados anteriores
        resultadoRepository.deleteByEstudianteId(estudianteId);
        
        // Obtener todas las carreras
        List<Carrera> carreras = carreraRepository.findAll();
        
        if (carreras.isEmpty()) {
            throw new RuntimeException("No hay carreras registradas en el sistema");
        }
        
        // Calcular para cada carrera
        List<Resultado> resultados = new ArrayList<>();
        
        for (Carrera carrera : carreras) {
            try {
                Resultado resultado = calcularPuntajeCarrera(estudianteId, carrera.getId());
                Resultado guardado = resultadoRepository.save(resultado);
                resultados.add(guardado);
            } catch (Exception e) {
                // Continuar con la siguiente carrera si falla una
                System.err.println("Error calculando carrera " + carrera.getNombre() + ": " + e.getMessage());
            }
        }
        
        return resultados;
    }
    
    /**
     * PASO 6: Obtener Top 3 de carreras recomendadas
     */
    public List<Resultado> obtenerTop3Carreras(Long estudianteId) {
        List<Resultado> resultados = resultadoRepository.findByEstudianteIdOrderByPuntajeFinalDesc(estudianteId);
        
        if (resultados.isEmpty()) {
            // Si no hay resultados, calcularlos primero
            calcularYGuardarResultados(estudianteId);
            resultados = resultadoRepository.findByEstudianteIdOrderByPuntajeFinalDesc(estudianteId);
        }
        
        // Retornar top 3
        return resultados.stream().limit(3).toList();
    }
    
    /**
     * Validar que el estudiante tiene datos suficientes
     */
    private void validarDatosEstudiante(Long estudianteId) {
        // Validar que existe el estudiante
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + estudianteId);
        }
        
        // Validar que tiene notas
        List<Nota> notas = notaRepository.findByEstudianteId(estudianteId);
        if (notas.isEmpty()) {
            throw new RuntimeException(
                "El estudiante debe tener notas registradas para calcular resultados"
            );
        }
        
        // Validar que ha respondido el test de habilidades
        List<RespuestaHabilidad> respuestas = respuestaHabilidadRepository.findByEstudianteId(estudianteId);
        if (respuestas.isEmpty()) {
            throw new RuntimeException(
                "El estudiante debe completar el test de habilidades para calcular resultados"
            );
        }
        
        // Validar que tiene afinidades
        List<Afinidad> afinidades = afinidadRepository.findByEstudianteId(estudianteId);
        if (afinidades.isEmpty()) {
            throw new RuntimeException(
                "El estudiante debe seleccionar al menos una afinidad para calcular resultados"
            );
        }
    }
    
    /**
     * Recalcular resultados (eliminar anteriores y calcular nuevos)
     */
    public List<Resultado> recalcularResultados(Long estudianteId) {
        return calcularYGuardarResultados(estudianteId);
    }
}
