package com.proyectoingweb.proyectoingweb.factory;

import com.proyectoingweb.proyectoingweb.entity.Carrera;
import com.proyectoingweb.proyectoingweb.entity.Estudiante;
import com.proyectoingweb.proyectoingweb.entity.Resultado;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory: Centraliza la creación de objetos Resultado.
 * Simplifica la construcción de resultados con diferentes configuraciones.
 * 
 * Beneficios:
 * - Construcción consistente de objetos complejos
 * - Validación centralizada
 * - Facilita testing y mantenimiento
 */
@Component
public class ResultadoFactory {
    
    /**
     * Crea un resultado con todos los componentes calculados.
     */
    public Resultado createResultado(Estudiante estudiante, Carrera carrera,
                                    float puntajeAcademico, float puntajeHabilidades,
                                    float puntajeAfinidad, float puntajeFinal) {
        Resultado resultado = new Resultado();
        resultado.setEstudiante(estudiante);
        resultado.setCarrera(carrera);
        resultado.setPuntajeAcademico(puntajeAcademico);
        resultado.setPuntajeHabilidades(puntajeHabilidades);
        resultado.setPuntajeAfinidad(puntajeAfinidad);
        resultado.setPuntajeFinal(puntajeFinal);
        return resultado;
    }
    
    /**
     * Crea un resultado vacío (inicializado con ceros).
     * Útil para casos donde no hay datos suficientes para calcular.
     */
    public Resultado createEmptyResultado(Estudiante estudiante, Carrera carrera) {
        Resultado resultado = new Resultado();
        resultado.setEstudiante(estudiante);
        resultado.setCarrera(carrera);
        resultado.setPuntajeAcademico(0.0f);
        resultado.setPuntajeHabilidades(0.0f);
        resultado.setPuntajeAfinidad(0.0f);
        resultado.setPuntajeFinal(0.0f);
        return resultado;
    }
    
    /**
     * Crea un resultado con puntaje final calculado automáticamente.
     * Aplica la fórmula estándar: 0.5*académico + 0.3*habilidades + 0.2*afinidad
     */
    public Resultado createResultadoConFormula(Estudiante estudiante, Carrera carrera,
                                               float puntajeAcademico, float puntajeHabilidades,
                                               float puntajeAfinidad) {
        float puntajeFinal = (0.5f * puntajeAcademico) + 
                            (0.3f * puntajeHabilidades) + 
                            (0.2f * puntajeAfinidad);
        
        return createResultado(estudiante, carrera, puntajeAcademico, 
                             puntajeHabilidades, puntajeAfinidad, puntajeFinal);
    }
    
    /**
     * Crea un resultado solo con componente académico (otros en cero).
     * Útil para evaluaciones parciales.
     */
    public Resultado createResultadoAcademicoOnly(Estudiante estudiante, Carrera carrera,
                                                  float puntajeAcademico) {
        Resultado resultado = new Resultado();
        resultado.setEstudiante(estudiante);
        resultado.setCarrera(carrera);
        resultado.setPuntajeAcademico(puntajeAcademico);
        resultado.setPuntajeHabilidades(0.0f);
        resultado.setPuntajeAfinidad(0.0f);
        resultado.setPuntajeFinal(0.5f * puntajeAcademico); // Solo componente académico
        return resultado;
    }
}
