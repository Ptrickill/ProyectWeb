package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.*;
import com.proyectoingweb.proyectoingweb.factory.ResultadoFactory;
import com.proyectoingweb.proyectoingweb.repository.*;
import com.proyectoingweb.proyectoingweb.strategy.ComponenteAcademicoStrategy;
import com.proyectoingweb.proyectoingweb.strategy.ComponenteAfinidadStrategy;
import com.proyectoingweb.proyectoingweb.strategy.ComponenteCalculoStrategy;
import com.proyectoingweb.proyectoingweb.strategy.ComponenteHabilidadesStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para el cálculo vocacional del CORE del sistema.
 * Implementa la fórmula: Score = 0.5×Académico + 0.3×Habilidades + 0.2×Afinidad
 * 
 * PATRONES IMPLEMENTADOS:
 * - Strategy Pattern: Diferentes estrategias de cálculo intercambiables
 * - Factory Pattern: Creación centralizada de objetos Resultado
 * - Dependency Inversion Principle: Inyección por constructor
 */
@Service
@Transactional
public class CalculoVocacionalService {
    
    // Repositorios
    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final NotaRepository notaRepository;
    private final RespuestaHabilidadRepository respuestaHabilidadRepository;
    private final AfinidadRepository afinidadRepository;
    private final ResultadoRepository resultadoRepository;
    
    // Estrategias de cálculo (Strategy Pattern)
    private final ComponenteCalculoStrategy estrategiaAcademica;
    private final ComponenteCalculoStrategy estrategiaHabilidades;
    private final ComponenteCalculoStrategy estrategiaAfinidad;
    
    // Factory para creación de resultados (Factory Pattern)
    private final ResultadoFactory resultadoFactory;
    
    // Inyección por constructor (mejora DIP - Dependency Inversion Principle)
    public CalculoVocacionalService(
            EstudianteRepository estudianteRepository,
            CarreraRepository carreraRepository,
            NotaRepository notaRepository,
            RespuestaHabilidadRepository respuestaHabilidadRepository,
            AfinidadRepository afinidadRepository,
            ResultadoRepository resultadoRepository,
            ComponenteAcademicoStrategy estrategiaAcademica,
            ComponenteHabilidadesStrategy estrategiaHabilidades,
            ComponenteAfinidadStrategy estrategiaAfinidad,
            ResultadoFactory resultadoFactory) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.notaRepository = notaRepository;
        this.respuestaHabilidadRepository = respuestaHabilidadRepository;
        this.afinidadRepository = afinidadRepository;
        this.resultadoRepository = resultadoRepository;
        this.estrategiaAcademica = estrategiaAcademica;
        this.estrategiaHabilidades = estrategiaHabilidades;
        this.estrategiaAfinidad = estrategiaAfinidad;
        this.resultadoFactory = resultadoFactory;
    }
    
    // Constantes de la fórmula
    private static final float PESO_ACADEMICO = 0.5f;
    private static final float PESO_HABILIDADES = 0.3f;
    private static final float PESO_AFINIDAD = 0.2f;
    
    /**
     * PASO 1: Calcular componente académico (50%)
     * Usa Strategy Pattern para delegar el cálculo
     */
    public float calcularComponenteAcademico(Long estudianteId, Long carreraId) {
        return estrategiaAcademica.calcular(estudianteId, carreraId);
    }
    
    /**
     * PASO 2: Calcular componente habilidades (30%)
     * Usa Strategy Pattern para delegar el cálculo
     */
    public float calcularComponenteHabilidades(Long estudianteId, Long carreraId) {
        return estrategiaHabilidades.calcular(estudianteId, carreraId);
    }
    
    /**
     * PASO 3: Calcular componente afinidad (20%)
     * Usa Strategy Pattern para delegar el cálculo
     */
    public float calcularComponenteAfinidad(Long estudianteId, Long carreraId) {
        return estrategiaAfinidad.calcular(estudianteId, carreraId);
    }
    
    /**
     * PASO 4: Calcular puntaje final para una carrera
     * Aplica la fórmula: Score = 0.5×Académico + 0.3×Habilidades + 0.2×Afinidad
     * Usa Factory Pattern para crear el objeto Resultado
     */
    public Resultado calcularPuntajeCarrera(Long estudianteId, Long carreraId) {
        // Validar que el estudiante existe
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + estudianteId));
        
        // Validar que la carrera existe
        Carrera carrera = carreraRepository.findById(carreraId)
            .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + carreraId));
        
        // Calcular componentes usando Strategy Pattern
        float puntajeAcademico = calcularComponenteAcademico(estudianteId, carreraId);
        float puntajeHabilidades = calcularComponenteHabilidades(estudianteId, carreraId);
        float puntajeAfinidad = calcularComponenteAfinidad(estudianteId, carreraId);
        
        // Usar Factory Pattern para crear el resultado
        return resultadoFactory.createResultadoConFormula(
            estudiante, carrera, puntajeAcademico, puntajeHabilidades, puntajeAfinidad
        );
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
