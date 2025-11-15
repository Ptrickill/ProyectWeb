package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.CarreraMateria;
import com.proyectoingweb.proyectoingweb.repository.CarreraMateriaRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraRepository;
import com.proyectoingweb.proyectoingweb.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarreraMateriaService {
    
    @Autowired
    private CarreraMateriaRepository carreraMateriaRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    // Obtener todos los pesos
    public List<CarreraMateria> getAllPesos() {
        return carreraMateriaRepository.findAll();
    }
    
    // Obtener peso por ID
    public Optional<CarreraMateria> getPesoById(Long id) {
        return carreraMateriaRepository.findById(id);
    }
    
    // Obtener pesos de una carrera
    public List<CarreraMateria> getPesosByCarrera(Long carreraId) {
        return carreraMateriaRepository.findByCarreraId(carreraId);
    }
    
    // ========== VALIDACIÓN: PESO EN RANGO VÁLIDO (0-100) ==========
    public CarreraMateria createPeso(CarreraMateria peso) {
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
        
        // Validar que la materia existe
        if (!materiaRepository.existsById(peso.getMateria().getId())) {
            throw new RuntimeException("La materia no existe");
        }
        
        // Evitar duplicados
        if (carreraMateriaRepository.existsByCarreraIdAndMateriaId(
            peso.getCarrera().getId(), peso.getMateria().getId())) {
            throw new RuntimeException(
                "Ya existe un peso para esta combinación de carrera y materia"
            );
        }
        
        return carreraMateriaRepository.save(peso);
    }
    
    // Actualizar peso
    public CarreraMateria updatePeso(Long id, CarreraMateria pesoActualizado) {
        return carreraMateriaRepository.findById(id)
            .map(peso -> {
                // Validar rango de peso (0-100)
                if (pesoActualizado.getPeso() < 0 || pesoActualizado.getPeso() > 100) {
                    throw new RuntimeException(
                        "El peso debe estar entre 0 y 100. Valor recibido: " + pesoActualizado.getPeso()
                    );
                }
                
                peso.setPeso(pesoActualizado.getPeso());
                return carreraMateriaRepository.save(peso);
            })
            .orElseThrow(() -> new RuntimeException("Peso no encontrado con ID: " + id));
    }
    
    // Eliminar peso
    public void deletePeso(Long id) {
        carreraMateriaRepository.deleteById(id);
    }
}
