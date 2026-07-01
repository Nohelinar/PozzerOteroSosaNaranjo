package tuti.desi.controller.publicaciones;

import tuti.desi.entities.enums.EstadoPublicacion;

public class PublicacionBuscarForm {
    private Long propiedadId;
    private String ciudad;
    private EstadoPublicacion estado;
    private Double precioMin;
    private Double precioMax;

    // Getters y Setters
    public Long getPropiedadId() { return propiedadId; }
    public void setPropiedadId(Long propiedadId) { this.propiedadId = propiedadId; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }

    public Double getPrecioMin() { return precioMin; }
    public void setPrecioMin(Double precioMin) { this.precioMin = precioMin; }

    public Double getPrecioMax() { return precioMax; }
    public void setPrecioMax(Double precioMax) { this.precioMax = precioMax; }
}