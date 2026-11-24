package com.androidApp.oportunidades_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "oportunidades")
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String tipo;

    @Column(length = 1000)
    private String descripcion;

    private String fuente;
    private String prioridad;
    private String potencial;
    private String estado;

    @Column(length = 1000)
    private String observaciones;

    @Column(length = 500)
    private String fotoUri;

    public Oportunidad() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getPotencial() { return potencial; }
    public void setPotencial(String potencial) { this.potencial = potencial; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFotoUri() { return fotoUri; }
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }
}
