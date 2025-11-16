package com.proyectoingweb.proyectoingweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preguntas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El texto de la pregunta es obligatorio")
    @Column(name = "texto", nullable = false, length = 500)
    private String texto;
    
    @NotBlank(message = "El tipo de pregunta es obligatorio")
    @Column(name = "tipo_pregunta", nullable = false)
    private String tipoPregunta; // GUSTO o AUTOEVALUACION
    
    @NotNull(message = "La habilidad es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habilidad_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "preguntas"})
    private Habilidad habilidad;
    
    // Relaciones
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaHabilidad> respuestas = new ArrayList<>();
    
    // Constructores
    public Pregunta() {}
    
    public Pregunta(String texto, String tipoPregunta, Habilidad habilidad) {
        this.texto = texto;
        this.tipoPregunta = tipoPregunta;
        this.habilidad = habilidad;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public String getTipoPregunta() {
        return tipoPregunta;
    }
    
    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }
    
    public Habilidad getHabilidad() {
        return habilidad;
    }
    
    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }
    
    public List<RespuestaHabilidad> getRespuestas() {
        return respuestas;
    }
    
    public void setRespuestas(List<RespuestaHabilidad> respuestas) {
        this.respuestas = respuestas;
    }
    
    @Override
    public String toString() {
        return "Pregunta{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", tipoPregunta='" + tipoPregunta + '\'' +
                '}';
    }
}
