package com.proyectoingweb.proyectoingweb.service;

import com.proyectoingweb.proyectoingweb.entity.Carrera;
import com.proyectoingweb.proyectoingweb.repository.CarreraAfinidadRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraHabilidadRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraMateriaRepository;
import com.proyectoingweb.proyectoingweb.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarreraService {
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private CarreraMateriaRepository carreraMateriaRepository;
    
    @Autowired
    private CarreraHabilidadRepository carreraHabilidadRepository;
    
    @Autowired
    private CarreraAfinidadRepository carreraAfinidadRepository;
    
    // Obtener todas las carreras
    public List<Carrera> getAllCarreras() {
        return carreraRepository.findAll();
    }
    
    // Obtener carrera por ID
    public Optional<Carrera> getCarreraById(Long id) {
        return carreraRepository.findById(id);
    }
    
    // ========== VALIDACIÓN CRÍTICA 1: CÓDIGO ÚNICO DE CARRERA ==========
    public Carrera createCarrera(Carrera carrera) {
        // Validar código único
        if (carreraRepository.existsByCodigo(carrera.getCodigo())) {
            throw new RuntimeException("Ya existe una carrera con el código: " + carrera.getCodigo());
        }
        
        // Validar nombre único
        if (carreraRepository.existsByNombre(carrera.getNombre())) {
            throw new RuntimeException("Ya existe una carrera con el nombre: " + carrera.getNombre());
        }
        
        return carreraRepository.save(carrera);
    }
    
    // Actualizar carrera
    public Carrera updateCarrera(Long id, Carrera carreraActualizada) {
        return carreraRepository.findById(id)
            .map(carrera -> {
                // Validar código único si cambió
                if (!carrera.getCodigo().equals(carreraActualizada.getCodigo()) &&
                    carreraRepository.existsByCodigo(carreraActualizada.getCodigo())) {
                    throw new RuntimeException("El código ya está en uso: " + carreraActualizada.getCodigo());
                }
                
                // Validar nombre único si cambió
                if (!carrera.getNombre().equals(carreraActualizada.getNombre()) &&
                    carreraRepository.existsByNombre(carreraActualizada.getNombre())) {
                    throw new RuntimeException("El nombre ya está en uso: " + carreraActualizada.getNombre());
                }
                
                carrera.setCodigo(carreraActualizada.getCodigo());
                carrera.setNombre(carreraActualizada.getNombre());
                carrera.setDescripcion(carreraActualizada.getDescripcion());
                return carreraRepository.save(carrera);
            })
            .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + id));
    }
    
    // Eliminar carrera
    public void deleteCarrera(Long id) {
        carreraRepository.deleteById(id);
    }
    
    // ========== VALIDACIÓN CRÍTICA 2: VALIDAR PESOS DE CARRERA ==========
    public void validarPesosCarrera(Long carreraId) {
        // Verificar que la carrera existe
        if (!carreraRepository.existsById(carreraId)) {
            throw new RuntimeException("La carrera no existe con ID: " + carreraId);
        }
        
        // Validar que tenga al menos un peso académico
        long pesosMaterias = carreraMateriaRepository.countByCarreraId(carreraId);
        if (pesosMaterias == 0) {
            throw new RuntimeException("La carrera debe tener al menos un peso académico asignado");
        }
        
        // Validar que tenga al menos un peso de habilidad
        long pesosHabilidades = carreraHabilidadRepository.countByCarreraId(carreraId);
        if (pesosHabilidades == 0) {
            throw new RuntimeException("La carrera debe tener al menos un peso de habilidad asignado");
        }
        
        // Validar que tenga al menos un peso de afinidad
        long pesosAfinidades = carreraAfinidadRepository.countByCarreraId(carreraId);
        if (pesosAfinidades == 0) {
            throw new RuntimeException("La carrera debe tener al menos un peso de afinidad asignado");
        }
    }
    
    // Verificar si una carrera está completamente configurada
    public boolean isCarreraCompleta(Long carreraId) {
        try {
            validarPesosCarrera(carreraId);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
    
    // Verificar si existe por código
    public boolean existsByCodigo(String codigo) {
        return carreraRepository.existsByCodigo(codigo);
    }
    
    // Verificar si existe por nombre
    public boolean existsByNombre(String nombre) {
        return carreraRepository.existsByNombre(nombre);
    }
}
