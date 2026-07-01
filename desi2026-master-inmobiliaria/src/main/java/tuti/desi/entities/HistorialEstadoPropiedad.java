package tuti.desi.entities;


import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import tuti.desi.entities.enums.EstadoDisponibilidad;

@Entity
public class HistorialEstadoPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDisponibilidad estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    // Getters y Setters
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

    public EstadoDisponibilidad getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoDisponibilidad estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoDisponibilidad getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoDisponibilidad estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
