package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.CarreraAfinidad;
import com.proyectoingweb.proyectoingweb.repository.CarreraAfinidadRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarreraAfinidadService {
    
    @Autowired
    private CarreraAfinidadRepository carreraAfinidadRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    // Obtener todos los pesos
    public List<CarreraAfinidad> getAllPesos() {
        return carreraAfinidadRepository.findAll();
    }
    
    // Obtener peso por ID
    public Optional<CarreraAfinidad> getPesoById(Long id) {
        return carreraAfinidadRepository.findById(id);
    }
    
    // Obtener pesos de una carrera
    public List<CarreraAfinidad> getPesosByCarrera(Long carreraId) {
        return carreraAfinidadRepository.findByCarreraId(carreraId);
    }
    
    // ========== VALIDACIÓN: PESO EN RANGO VÁLIDO (0-100) ==========
    public CarreraAfinidad createPeso(CarreraAfinidad peso) {
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
        
        // Evitar duplicados
        if (carreraAfinidadRepository.existsByCarreraIdAndArea(
            peso.getCarrera().getId(), peso.getArea())) {
            throw new RuntimeException(
                "Ya existe un peso para esta combinación de carrera y área"
            );
        }
        
        return carreraAfinidadRepository.save(peso);
    }
    
    // Actualizar peso
    public CarreraAfinidad updatePeso(Long id, CarreraAfinidad pesoActualizado) {
        return carreraAfinidadRepository.findById(id)
            .map(peso -> {
                // Validar rango de peso (0-100)
                if (pesoActualizado.getPeso() < 0 || pesoActualizado.getPeso() > 100) {
                    throw new RuntimeException(
                        "El peso debe estar entre 0 y 100. Valor recibido: " + pesoActualizado.getPeso()
                    );
                }
                
                peso.setPeso(pesoActualizado.getPeso());
                peso.setArea(pesoActualizado.getArea());
                return carreraAfinidadRepository.save(peso);
            })
            .orElseThrow(() -> new RuntimeException("Peso no encontrado con ID: " + id));
    }
    
    // Eliminar peso
    public void deletePeso(Long id) {
        carreraAfinidadRepository.deleteById(id);
    }
}
