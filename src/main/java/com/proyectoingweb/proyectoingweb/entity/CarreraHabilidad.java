package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "carrera_habilidad", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"carrera_id", "habilidad_id"})
})
public class CarreraHabilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.0", message = "El peso mínimo es 0")
    @DecimalMax(value = "100.0", message = "El peso máximo es 100")
    @Column(name = "peso", nullable = false)
    private Float peso; // Peso de la habilidad para esta carrera (0-100)
    
    @NotNull(message = "La carrera es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    
    @NotNull(message = "La habilidad es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habilidad_id", nullable = false)
    private Habilidad habilidad;
    
    // Constructores
    public CarreraHabilidad() {}
    
    public CarreraHabilidad(Float peso, Carrera carrera, Habilidad habilidad) {
        this.peso = peso;
        this.carrera = carrera;
        this.habilidad = habilidad;
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
    
    public Habilidad getHabilidad() {
        return habilidad;
    }
    
    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }
    
    @Override
    public String toString() {
        return "CarreraHabilidad{" +
                "id=" + id +
                ", peso=" + peso +
                '}';
    }
}
