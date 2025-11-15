package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "respuestas_habilidad", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "pregunta_id"})
})
public class RespuestaHabilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El puntaje es obligatorio")
    @Min(value = 1, message = "El puntaje mínimo es 1")
    @Max(value = 5, message = "El puntaje máximo es 5")
    @Column(name = "puntaje", nullable = false)
    private Integer puntaje; // Escala 1-5
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @NotNull(message = "La pregunta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;
    
    // Constructores
    public RespuestaHabilidad() {}
    
    public RespuestaHabilidad(Integer puntaje, Estudiante estudiante, Pregunta pregunta) {
        this.puntaje = puntaje;
        this.estudiante = estudiante;
        this.pregunta = pregunta;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Pregunta getPregunta() {
        return pregunta;
    }
    
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    
    @Override
    public String toString() {
        return "RespuestaHabilidad{" +
                "id=" + id +
                ", puntaje=" + puntaje +
                '}';
    }
}
