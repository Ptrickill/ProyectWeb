package com.proyectoingweb.proyectoingweb.strategy;

/**
 * Patrón Strategy: Interfaz para estrategias de cálculo de componentes vocacionales.
 * Permite intercambiar diferentes algoritmos de cálculo sin modificar el código cliente.
 * 
 * Principio SOLID: Open/Closed Principle - abierto a extensión, cerrado a modificación
 */
public interface ComponenteCalculoStrategy {
    
    /**
     * Calcula el componente específico para un estudiante y carrera.
     * 
     * @param estudianteId ID del estudiante
     * @param carreraId ID de la carrera
     * @return Puntaje normalizado entre 0 y 1
     */
    float calcular(Long estudianteId, Long carreraId);
    
    /**
     * Retorna el nombre descriptivo del componente.
     * 
     * @return Nombre del componente (ej: "Académico", "Habilidades", "Afinidad")
     */
    String getNombreComponente();
}
