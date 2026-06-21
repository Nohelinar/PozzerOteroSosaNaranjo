
package com.tuti.desi.pozzeroterososanaranjo.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;

@Entity
public class HistorialEstadoPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    @Enumerated(EnumType.STRING)
    private EstadoPropiedad estadoPropiedad;

    private LocalDateTime fechaCambio;

    public Long getId() {
        return id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public EstadoPropiedad getEstadoPropiedad() {
        return estadoPropiedad;
    }

    public void setEstadoPropiedad(EstadoPropiedad estadoPropiedad) {
        this.estadoPropiedad = estadoPropiedad;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
