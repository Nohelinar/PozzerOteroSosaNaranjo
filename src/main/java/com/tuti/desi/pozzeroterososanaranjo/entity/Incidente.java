package com.tuti.desi.pozzeroterososanaranjo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.tuti.desi.pozzeroterososanaranjo.enums.CategoriaIncidente;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoIncidente;
import com.tuti.desi.pozzeroterososanaranjo.enums.PrioridadIncidente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    private String titulo;

    @Lob
    @Column(length = 4000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private CategoriaIncidente categoria;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaAlta;

    @Enumerated(EnumType.STRING)
    private PrioridadIncidente prioridad;

    @Enumerated(EnumType.STRING)
    private EstadoIncidente estado;

    private boolean eliminado;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaResolucion;

    @Lob
    @Column(length = 4000)
    private String observacionesResolucion;

    private BigDecimal costoResolucion;

    private String responsableTecnico;

    @OneToMany(mappedBy = "incidente")
    private List<HistorialEstadoIncidente> historialEstados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaIncidente getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaIncidente categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public PrioridadIncidente getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadIncidente prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoIncidente getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidente estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getObservacionesResolucion() {
        return observacionesResolucion;
    }

    public void setObservacionesResolucion(String observacionesResolucion) {
        this.observacionesResolucion = observacionesResolucion;
    }

    public BigDecimal getCostoResolucion() {
        return costoResolucion;
    }

    public void setCostoResolucion(BigDecimal costoResolucion) {
        this.costoResolucion = costoResolucion;
    }

    public String getResponsableTecnico() {
        return responsableTecnico;
    }

    public void setResponsableTecnico(String responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

    public List<HistorialEstadoIncidente> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<HistorialEstadoIncidente> historialEstados) {
        this.historialEstados = historialEstados;
    }
}
