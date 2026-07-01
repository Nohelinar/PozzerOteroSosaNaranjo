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

import tuti.desi.entities.enums.EstadoFactura;

@Entity
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    // Constructor
    public HistorialEstadoFactura() {
        this.fechaCambio = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public EstadoFactura getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoFactura estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public EstadoFactura getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoFactura estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}