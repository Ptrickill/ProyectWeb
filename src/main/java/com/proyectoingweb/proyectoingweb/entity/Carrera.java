package com.proyectoingweb.proyectoingweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carreras")
public class Carrera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El código de la carrera es obligatorio")
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;
    
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    // Relaciones
    @JsonIgnore
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarreraMateria> carreraMaterias = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarreraHabilidad> carreraHabilidades = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarreraAfinidad> carreraAfinidades = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resultado> resultados = new ArrayList<>();
    
    // Constructores
    public Carrera() {}
    
    public Carrera(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
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
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
    public List<CarreraMateria> getCarreraMaterias() {
        return carreraMaterias;
    }
    
    public void setCarreraMaterias(List<CarreraMateria> carreraMaterias) {
        this.carreraMaterias = carreraMaterias;
    }
    
    public List<CarreraHabilidad> getCarreraHabilidades() {
        return carreraHabilidades;
    }
    
    public void setCarreraHabilidades(List<CarreraHabilidad> carreraHabilidades) {
        this.carreraHabilidades = carreraHabilidades;
    }
    
    public List<CarreraAfinidad> getCarreraAfinidades() {
        return carreraAfinidades;
    }
    
    public void setCarreraAfinidades(List<CarreraAfinidad> carreraAfinidades) {
        this.carreraAfinidades = carreraAfinidades;
    }
    
    public List<Resultado> getResultados() {
        return resultados;
    }
    
    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }
    
    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
