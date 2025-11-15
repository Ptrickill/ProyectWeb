package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "notas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "materia_id"})
})
public class Nota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", message = "La calificación mínima es 0")
    @DecimalMax(value = "10.0", message = "La calificación máxima es 10")
    @Column(name = "calificacion", nullable = false)
    private Float calificacion;
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @NotNull(message = "La materia es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
    
    // Constructores
    public Nota() {}
    
    public Nota(Float calificacion, Estudiante estudiante, Materia materia) {
        this.calificacion = calificacion;
        this.estudiante = estudiante;
        this.materia = materia;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Float getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Materia getMateria() {
        return materia;
    }
    
    public void setMateria(Materia materia) {
        this.materia = materia;
    }
    
    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", calificacion=" + calificacion +
                '}';
    }
}
