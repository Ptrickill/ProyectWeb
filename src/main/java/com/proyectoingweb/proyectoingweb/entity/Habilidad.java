package com.proyectoingweb.proyectoingweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habilidades")
public class Habilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la habilidad es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    // Relaciones
    @JsonIgnore
    @OneToMany(mappedBy = "habilidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pregunta> preguntas = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "habilidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarreraHabilidad> carreraHabilidades = new ArrayList<>();
    
    // Constructores
    public Habilidad() {}
    
    public Habilidad(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
    
    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
    
    public List<CarreraHabilidad> getCarreraHabilidades() {
        return carreraHabilidades;
    }
    
    public void setCarreraHabilidades(List<CarreraHabilidad> carreraHabilidades) {
        this.carreraHabilidades = carreraHabilidades;
    }
    
    @Override
    public String toString() {
        return "Habilidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
