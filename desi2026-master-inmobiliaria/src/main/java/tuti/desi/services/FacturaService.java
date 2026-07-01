package tuti.desi.services;

import java.time.LocalDate;
import java.util.List;

import tuti.desi.entities.Factura;
import tuti.desi.entities.enums.EstadoFactura;
import tuti.desi.entities.enums.MedioPago;
import tuti.desi.exceptions.Excepcion;

public interface FacturaService {
    Factura save(Factura factura) throws Excepcion;
    Factura getById(Long id) throws Excepcion;
    List<Factura> getAll();
    List<Factura> getAllActivas();
    void deleteById(Long id) throws Excepcion;
    List<Factura> filter(Long contratoId, Long propiedadId, Long inquilinoId, EstadoFactura estado, LocalDate fechaVencimientoDesde, LocalDate fechaVencimientoHasta);
    void cambiarEstado(Factura factura, EstadoFactura nuevoEstado) throws Excepcion;
    void registrarPago(Long facturaId, LocalDate fechaPago, MedioPago medioPago, Double importePagado, Double interes) throws Excepcion;
}