package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.CarreraHabilidad;
import com.proyectoingweb.proyectoingweb.repository.CarreraHabilidadRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraRepository;
import com.proyectoingweb.proyectoingweb.repository.HabilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarreraHabilidadService {
    
    @Autowired
    private CarreraHabilidadRepository carreraHabilidadRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private HabilidadRepository habilidadRepository;
    
    // Obtener todos los pesos
    public List<CarreraHabilidad> getAllPesos() {
        return carreraHabilidadRepository.findAll();
    }
    
    // Obtener peso por ID
    public Optional<CarreraHabilidad> getPesoById(Long id) {
        return carreraHabilidadRepository.findById(id);
    }
    
    // Obtener pesos de una carrera
    public List<CarreraHabilidad> getPesosByCarrera(Long carreraId) {
        return carreraHabilidadRepository.findByCarreraId(carreraId);
    }
    
    // ========== VALIDACIÓN: PESO EN RANGO VÁLIDO (0-100) ==========
    public CarreraHabilidad createPeso(CarreraHabilidad peso) {
        // Validar rango de peso (0-100)
        if (peso.getPeso() < 0 || peso.getPeso() > 100) {
            throw new RuntimeException(
                "El peso debe estar entre 0 y 100. Valor recibido: " + peso.getPeso()
            );
        }
        
        // Validar que la carrera existe
        if (!carreraRepository.existsById(peso.getCarrera().getId())) {
            throw new RuntimeException("La carrera no existe");
        }
        
        // Validar que la habilidad existe
        if (!habilidadRepository.existsById(peso.getHabilidad().getId())) {
            throw new RuntimeException("La habilidad no existe");
        }
        
        // Evitar duplicados
        if (carreraHabilidadRepository.existsByCarreraIdAndHabilidadId(
            peso.getCarrera().getId(), peso.getHabilidad().getId())) {
            throw new RuntimeException(
                "Ya existe un peso para esta combinación de carrera y habilidad"
            );
        }
        
        return carreraHabilidadRepository.save(peso);
    }
    
    // Actualizar peso
    public CarreraHabilidad updatePeso(Long id, CarreraHabilidad pesoActualizado) {
        return carreraHabilidadRepository.findById(id)
            .map(peso -> {
                // Validar rango de peso (0-100)
                if (pesoActualizado.getPeso() < 0 || pesoActualizado.getPeso() > 100) {
                    throw new RuntimeException(
                        "El peso debe estar entre 0 y 100. Valor recibido: " + pesoActualizado.getPeso()
                    );
                }
                
                peso.setPeso(pesoActualizado.getPeso());
                return carreraHabilidadRepository.save(peso);
            })
            .orElseThrow(() -> new RuntimeException("Peso no encontrado con ID: " + id));
    }
    
    // Eliminar peso
    public void deletePeso(Long id) {
        carreraHabilidadRepository.deleteById(id);
    }
}
