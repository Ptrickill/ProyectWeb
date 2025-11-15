package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "carrera_afinidad", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"carrera_id", "area"})
})
public class CarreraAfinidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El área es obligatoria")
    @Column(name = "area", nullable = false)
    private String area; // Matemáticas, Lengua, Ciencias, Sociales, Arte, Tecnología
    
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.0", message = "El peso mínimo es 0")
    @DecimalMax(value = "100.0", message = "El peso máximo es 100")
    @Column(name = "peso", nullable = false)
    private Float peso; // Peso del área de afinidad para esta carrera (0-100)
    
    @NotNull(message = "La carrera es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    
    // Constructores
    public CarreraAfinidad() {}
    
    public CarreraAfinidad(String area, Float peso, Carrera carrera) {
        this.area = area;
        this.peso = peso;
        this.carrera = carrera;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getArea() {
        return area;
    }
    
    public void setArea(String area) {
        this.area = area;
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
    
    @Override
    public String toString() {
        return "CarreraAfinidad{" +
                "id=" + id +
                ", area='" + area + '\'' +
                ", peso=" + peso +
                '}';
    }
}
