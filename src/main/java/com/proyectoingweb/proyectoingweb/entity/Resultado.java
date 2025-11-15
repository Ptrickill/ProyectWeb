package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
public class Resultado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El puntaje final es obligatorio")
    @Column(name = "puntaje_final", nullable = false)
    private Float puntajeFinal;
    
    @Column(name = "puntaje_academico")
    private Float puntajeAcademico;
    
    @Column(name = "puntaje_habilidades")
    private Float puntajeHabilidades;
    
    @Column(name = "puntaje_afinidad")
    private Float puntajeAfinidad;
    
    @Column(name = "fecha_calculo")
    private LocalDateTime fechaCalculo = LocalDateTime.now();
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @NotNull(message = "La carrera es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    
    // Constructores
    public Resultado() {}
    
    public Resultado(Float puntajeFinal, Estudiante estudiante, Carrera carrera) {
        this.puntajeFinal = puntajeFinal;
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
    
    public Float getPuntajeFinal() {
        return puntajeFinal;
    }
    
    public void setPuntajeFinal(Float puntajeFinal) {
        this.puntajeFinal = puntajeFinal;
    }
    
    public Float getPuntajeAcademico() {
        return puntajeAcademico;
    }
    
    public void setPuntajeAcademico(Float puntajeAcademico) {
        this.puntajeAcademico = puntajeAcademico;
    }
    
    public Float getPuntajeHabilidades() {
        return puntajeHabilidades;
    }
    
    public void setPuntajeHabilidades(Float puntajeHabilidades) {
        this.puntajeHabilidades = puntajeHabilidades;
    }
    
    public Float getPuntajeAfinidad() {
        return puntajeAfinidad;
    }
    
    public void setPuntajeAfinidad(Float puntajeAfinidad) {
        this.puntajeAfinidad = puntajeAfinidad;
    }
    
    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }
    
    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
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
        return "Resultado{" +
                "id=" + id +
                ", puntajeFinal=" + puntajeFinal +
                ", puntajeAcademico=" + puntajeAcademico +
                ", puntajeHabilidades=" + puntajeHabilidades +
                ", puntajeAfinidad=" + puntajeAfinidad +
                ", fechaCalculo=" + fechaCalculo +
                '}';
    }
}
