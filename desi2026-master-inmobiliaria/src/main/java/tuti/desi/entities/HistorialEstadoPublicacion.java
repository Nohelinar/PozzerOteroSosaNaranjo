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

import tuti.desi.entities.enums.EstadoPublicacion;

@Entity
public class HistorialEstadoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPublicacion estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    // Constructor
    public HistorialEstadoPublicacion() {
        this.fechaCambio = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }

    public EstadoPublicacion getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoPublicacion estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public EstadoPublicacion getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoPublicacion estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}