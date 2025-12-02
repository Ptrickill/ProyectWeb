package com.proyectoingweb.proyectoingweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "afinidades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "carrera_id"})
})
public class Afinidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El nivel de interés es obligatorio")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 5, message = "El nivel máximo es 5")
    @Column(name = "nivel_interes", nullable = false)
    private Integer nivelInteres; // 1-5 (1=muy bajo, 5=muy alto)
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @JsonIgnoreProperties({"notas", "respuestasHabilidad", "afinidades", "resultados"})
    private Estudiante estudiante;
    
    @NotNull(message = "La carrera es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrera_id", nullable = false)
    @JsonIgnoreProperties({"carreraMaterias", "carreraHabilidades", "carreraAfinidades", "resultados"})
    private Carrera carrera;
    
    // Constructores
    public Afinidad() {}
    
    public Afinidad(Integer nivelInteres, Estudiante estudiante, Carrera carrera) {
        this.nivelInteres = nivelInteres;
        this.estudiante = estudiante;
        this.carrera = carrera;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNivelInteres() {
        return nivelInteres;
    }
    
    public void setNivelInteres(Integer nivelInteres) {
        this.nivelInteres = nivelInteres;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Carrera getCarrera() {
        return carrera;
    }
    
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }
    
    @Override
    public String toString() {
        return "Afinidad{" +
                "id=" + id +
                ", nivelInteres=" + nivelInteres +
                ", carrera=" + (carrera != null ? carrera.getNombre() : "null") +
                '}';
    }
}
