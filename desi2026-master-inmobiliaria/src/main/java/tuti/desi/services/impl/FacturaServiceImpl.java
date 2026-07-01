package tuti.desi.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entities.Factura;
import tuti.desi.entities.Contrato;
import tuti.desi.entities.enums.EstadoFactura;
import tuti.desi.entities.enums.EstadoContrato;
import tuti.desi.entities.enums.MedioPago;
import tuti.desi.exceptions.EntidadNoEncontradaException;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.repository.FacturaRepository;
import tuti.desi.services.ContratoService;
import tuti.desi.services.FacturaService;

@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository repo;
    private final ContratoService contratoService;

    public FacturaServiceImpl(FacturaRepository repo, ContratoService contratoService) {
        this.repo = repo;
        this.contratoService = contratoService;
    }

    @Override
    @Transactional
    public Factura save(Factura factura) throws Excepcion {
        // Validar contrato
        if (factura.getContrato() == null || factura.getContrato().getId() == null) {
            throw new Excepcion("El contrato es obligatorio", "contrato");
        }

        Contrato contrato = contratoService.getById(factura.getContrato().getId());

        // Validar que el contrato esté activo
        if (contrato.getEstadoContrato() != EstadoContrato.Activo) {
            throw new Excepcion("Solo se pueden crear facturas para contratos activos", "contrato");
        }

        // Validar fechas
        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new Excepcion("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión", "fechaVencimiento");
        }

        // Si la factura se crea como Pagada, debe tener datos de pago
        if (factura.getEstadoFactura() == EstadoFactura.Pagada) {
            if (factura.getFechaPago() == null || factura.getMedioPago() == null || factura.getImportePagado() == null) {
                throw new Excepcion("Para registrar una factura como pagada, debe completar los datos de pago");
            }
        }

        // Guardar factura
        Factura saved = repo.save(factura);
        return saved;
    }

    @Override
    public Factura getById(Long id) throws Excepcion {
        return repo.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Factura", id));
    }

    @Override
    public List<Factura> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Factura> getAllActivas() {
        return repo.findAllActivas();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Factura factura = getById(id);

        // No se puede eliminar una factura pagada
        if (factura.getEstadoFactura() == EstadoFactura.Pagada) {
            throw new Excepcion("No se puede eliminar una factura pagada");
        }

        factura.setEliminado(true);
        repo.save(factura);
    }

    @Override
    public List<Factura> filter(Long contratoId, Long propiedadId, Long inquilinoId, EstadoFactura estado, LocalDate fechaVencimientoDesde, LocalDate fechaVencimientoHasta) {
        return repo.filter(contratoId, propiedadId, inquilinoId, estado, fechaVencimientoDesde, fechaVencimientoHasta);
    }

    @Override
    @Transactional
    public void cambiarEstado(Factura factura, EstadoFactura nuevoEstado) throws Excepcion {
        EstadoFactura estadoAnterior = factura.getEstadoFactura();

        // Validar transiciones permitidas
        if (estadoAnterior == EstadoFactura.Pendiente) {
            if (nuevoEstado == EstadoFactura.Pagada || nuevoEstado == EstadoFactura.Vencida || nuevoEstado == EstadoFactura.Anulada) {
                // OK
            } else {
                throw new Excepcion("No se permite cambiar de " + estadoAnterior + " a " + nuevoEstado);
            }
        } else if (estadoAnterior == EstadoFactura.Vencida) {
            if (nuevoEstado == EstadoFactura.Pagada) {
                // OK
            } else {
                throw new Excepcion("No se permite cambiar de " + estadoAnterior + " a " + nuevoEstado);
            }
        } else {
            throw new Excepcion("No se permite cambiar de " + estadoAnterior + " a " + nuevoEstado);
        }

        // Si pasa a Pagada, debe tener datos de pago
        if (nuevoEstado == EstadoFactura.Pagada) {
            if (factura.getFechaPago() == null || factura.getMedioPago() == null || factura.getImportePagado() == null) {
                throw new Excepcion("Para marcar una factura como pagada, debe completar los datos de pago");
            }
        }

        factura.setEstadoFactura(nuevoEstado);
        factura.agregarCambioEstado(estadoAnterior, nuevoEstado);
        repo.save(factura);
    }

    @Override
    @Transactional
    public void registrarPago(Long facturaId, LocalDate fechaPago, MedioPago medioPago, Double importePagado, Double interes) throws Excepcion {
        Factura factura = getById(facturaId);

        if (factura.getEstadoFactura() == EstadoFactura.Anulada) {
            throw new Excepcion("No se puede pagar una factura anulada");
        }

        if (factura.getEstadoFactura() == EstadoFactura.Pagada) {
            throw new Excepcion("La factura ya está pagada");
        }

        factura.setFechaPago(fechaPago);
        factura.setMedioPago(medioPago);
        factura.setImportePagado(importePagado);
        factura.setInteres(interes);

        // Cambiar estado a Pagada
        cambiarEstado(factura, EstadoFactura.Pagada);
    }
}