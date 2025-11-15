package com.proyectoingweb.proyectoingweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materias")
public class Materia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @NotBlank(message = "El área es obligatoria")
    @Column(name = "area", nullable = false)
    private String area; // Matemáticas, Lengua, Ciencias, Sociales, Arte, Tecnología
    
    // Relaciones
    @JsonIgnore
    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarreraMateria> carreraMaterias = new ArrayList<>();
    
    // Constructores
    public Materia() {}
    
    public Materia(String nombre, String area) {
        this.nombre = nombre;
        this.area = area;
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
    
    public String getArea() {
        return area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    public List<Nota> getNotas() {
        return notas;
    }
    
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }
    
    public List<CarreraMateria> getCarreraMaterias() {
        return carreraMaterias;
    }
    
    public void setCarreraMaterias(List<CarreraMateria> carreraMaterias) {
        this.carreraMaterias = carreraMaterias;
    }
    
    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
