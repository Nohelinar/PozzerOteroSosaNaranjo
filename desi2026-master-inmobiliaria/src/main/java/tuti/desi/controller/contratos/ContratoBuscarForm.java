package tuti.desi.controller.contratos;

import java.time.LocalDate;

import tuti.desi.entities.enums.EstadoContrato;

public class ContratoBuscarForm {
    private Long propiedadId;
    private Long inquilinoId;
    private EstadoContrato estado;
    private LocalDate fechaInicio;

    // Getters y Setters
    public Long getPropiedadId() { return propiedadId; }
    public void setPropiedadId(Long propiedadId) { this.propiedadId = propiedadId; }

    public Long getInquilinoId() { return inquilinoId; }
    public void setInquilinoId(Long inquilinoId) { this.inquilinoId = inquilinoId; }

    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
}