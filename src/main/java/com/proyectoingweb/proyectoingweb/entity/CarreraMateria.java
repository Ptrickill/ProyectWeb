package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "carrera_materia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"carrera_id", "materia_id"})
})
public class CarreraMateria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.0", message = "El peso mínimo es 0")
    @DecimalMax(value = "100.0", message = "El peso máximo es 100")
    @Column(name = "peso", nullable = false)
    private Float peso; // Peso de la materia para esta carrera (0-100)
    
    @NotNull(message = "La carrera es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    
    @NotNull(message = "La materia es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
    
    // Constructores
    public CarreraMateria() {}
    
    public CarreraMateria(Float peso, Carrera carrera, Materia materia) {
        this.peso = peso;
        this.carrera = carrera;
        this.materia = materia;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Float getPeso() {
        return peso;
    }
    
    public void setPeso(Float peso) {
        this.peso = peso;
    }
    
    public Carrera getCarrera() {
        return carrera;
    }
    
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }
    
    public Materia getMateria() {
        return materia;
    }
    
    public void setMateria(Materia materia) {
        this.materia = materia;
    }
    
    @Override
    public String toString() {
        return "CarreraMateria{" +
                "id=" + id +
                ", peso=" + peso +
                '}';
    }
}
