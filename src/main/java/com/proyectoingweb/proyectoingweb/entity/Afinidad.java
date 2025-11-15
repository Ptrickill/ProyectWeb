package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "afinidades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "area"})
})
public class Afinidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El área es obligatoria")
    @Column(name = "area", nullable = false)
    private String area; // Matemáticas, Lengua, Ciencias, Sociales, Arte, Tecnología
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    // Constructores
    public Afinidad() {}
    
    public Afinidad(String area, Estudiante estudiante) {
        this.area = area;
        this.estudiante = estudiante;
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
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    @Override
    public String toString() {
        return "Afinidad{" +
                "id=" + id +
                ", area='" + area + '\'' +
                '}';
    }
}
