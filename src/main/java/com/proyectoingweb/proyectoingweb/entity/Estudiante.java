package com.proyectoingweb.proyectoingweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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
    
    @Column(name = "apellido")
    private String apellido;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Min(value = 15, message = "La edad mínima es 15 años")
    @Column(name = "edad")
    private Integer edad = 18; // Edad por defecto
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    // Relaciones
    @JsonIgnore
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaHabilidad> respuestasHabilidad = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Afinidad> afinidades = new ArrayList<>();
    
    @JsonIgnore
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
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
