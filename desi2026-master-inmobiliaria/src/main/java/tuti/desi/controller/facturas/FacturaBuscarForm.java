package tuti.desi.controller.facturas;

import java.time.LocalDate;

import tuti.desi.entities.enums.EstadoFactura;

public class FacturaBuscarForm {
    private Long contratoId;
    private Long propiedadId;
    private Long inquilinoId;
    private EstadoFactura estado;
    private LocalDate fechaVencimientoDesde;
    private LocalDate fechaVencimientoHasta;

    // Getters y Setters
    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }

    public Long getPropiedadId() { return propiedadId; }
    public void setPropiedadId(Long propiedadId) { this.propiedadId = propiedadId; }

    public Long getInquilinoId() { return inquilinoId; }
    public void setInquilinoId(Long inquilinoId) { this.inquilinoId = inquilinoId; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public LocalDate getFechaVencimientoDesde() { return fechaVencimientoDesde; }
    public void setFechaVencimientoDesde(LocalDate fechaVencimientoDesde) { this.fechaVencimientoDesde = fechaVencimientoDesde; }

    public LocalDate getFechaVencimientoHasta() { return fechaVencimientoHasta; }
    public void setFechaVencimientoHasta(LocalDate fechaVencimientoHasta) { this.fechaVencimientoHasta = fechaVencimientoHasta; }
}