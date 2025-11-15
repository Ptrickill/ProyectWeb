package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 15, message = "La edad mínima es 15 años")
    @Column(name = "edad", nullable = false)
    private Integer edad;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    // Relaciones
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaHabilidad> respuestasHabilidad = new ArrayList<>();
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Afinidad> afinidades = new ArrayList<>();
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resultado> resultados = new ArrayList<>();
    
    // Constructores
    public Estudiante() {}
    
    public Estudiante(String nombre, Integer edad) {
        this.nombre = nombre;
        this.edad = edad;
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
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public List<Nota> getNotas() {
        return notas;
    }
    
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }
    
    public List<RespuestaHabilidad> getRespuestasHabilidad() {
        return respuestasHabilidad;
    }
    
    public void setRespuestasHabilidad(List<RespuestaHabilidad> respuestasHabilidad) {
        this.respuestasHabilidad = respuestasHabilidad;
    }
    
    public List<Afinidad> getAfinidades() {
        return afinidades;
    }
    
    public void setAfinidades(List<Afinidad> afinidades) {
        this.afinidades = afinidades;
    }
    
    public List<Resultado> getResultados() {
        return resultados;
    }
    
    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }
    
    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
