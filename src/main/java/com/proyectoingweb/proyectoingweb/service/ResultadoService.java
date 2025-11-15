package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Resultado;
import com.proyectoingweb.proyectoingweb.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResultadoService {
    
    @Autowired
    private ResultadoRepository resultadoRepository;
    
    // Obtener todos los resultados
    public List<Resultado> getAllResultados() {
        return resultadoRepository.findAll();
    }
    
    // Obtener resultado por ID
    public Optional<Resultado> getResultadoById(Long id) {
        return resultadoRepository.findById(id);
    }
    
    // Obtener resultados de un estudiante
    public List<Resultado> getResultadosByEstudiante(Long estudianteId) {
        return resultadoRepository.findByEstudianteId(estudianteId);
    }
    
    // Obtener Top N resultados de un estudiante (ordenados por puntaje)
    public List<Resultado> getTopResultadosByEstudiante(Long estudianteId, int limit) {
        List<Resultado> resultados = resultadoRepository.findByEstudianteIdOrderByPuntajeFinalDesc(estudianteId);
        return resultados.stream().limit(limit).toList();
    }
    
    // Obtener Top 3 resultados (CORE del sistema)
    public List<Resultado> getTop3Resultados(Long estudianteId) {
        return getTopResultadosByEstudiante(estudianteId, 3);
    }
    
    // Crear resultado
    public Resultado createResultado(Resultado resultado) {
        return resultadoRepository.save(resultado);
    }
    
    // Actualizar resultado
    public Resultado updateResultado(Long id, Resultado resultadoActualizado) {
        return resultadoRepository.findById(id)
            .map(resultado -> {
                resultado.setPuntajeFinal(resultadoActualizado.getPuntajeFinal());
                resultado.setPuntajeAcademico(resultadoActualizado.getPuntajeAcademico());
                resultado.setPuntajeHabilidades(resultadoActualizado.getPuntajeHabilidades());
                resultado.setPuntajeAfinidad(resultadoActualizado.getPuntajeAfinidad());
                return resultadoRepository.save(resultado);
            })
            .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + id));
    }
    
    // Eliminar resultado
    public void deleteResultado(Long id) {
        resultadoRepository.deleteById(id);
    }
    
    // Eliminar todos los resultados de un estudiante (para recalcular)
    public void deleteAllResultadosByEstudiante(Long estudianteId) {
        resultadoRepository.deleteByEstudianteId(estudianteId);
    }
}
