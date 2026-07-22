package com.tuti.desi.pozzeroterososanaranjo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Contrato;
import com.tuti.desi.pozzeroterososanaranjo.entity.Factura;
import com.tuti.desi.pozzeroterososanaranjo.entity.HistorialEstadoFactura;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoContrato;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoFactura;
import com.tuti.desi.pozzeroterososanaranjo.repository.ContratoRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.FacturaRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.HistorialEstadoFacturaRepository;

@Service
public class FacturaService {

	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private HistorialEstadoFacturaRepository historialEstadoFacturaRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Transactional
	public Factura altaFactura(Factura factura) {

		validarDatosObligatorios(factura);

		Contrato contrato = buscarYValidarContrato(factura.getContrato().getId());

		if (contrato.getEstado() != EstadoContrato.ACTIVO) {
			throw new RuntimeException(
					"No se puede crear una factura para un contrato finalizado, rescindido, eliminado o en borrador.");
		}

		factura.setContrato(contrato);
		factura.setEstado(EstadoFactura.PENDIENTE);
		factura.setEliminada(false);

		// Los datos de pago no se cargan en el alta: solo se completan al marcar la factura como pagada.
		factura.setFechaPago(null);
		factura.setMedioPago(null);
		factura.setImportePagado(null);
		factura.setInteresPagado(null);

		Factura facturaGuardada = facturaRepository.save(factura);

		guardarHistorialEstado(facturaGuardada);

		return facturaGuardada;
	}

	@Transactional
	public void eliminarFactura(Long id) {

		Factura factura = facturaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La factura buscada no existe."));

		if (factura.isEliminada()) {
			throw new RuntimeException("La factura ya se encuentra eliminada.");
		}

		if (factura.getEstado() == EstadoFactura.PAGADA) {
			throw new RuntimeException("No se puede eliminar una factura pagada.");
		}

		factura.setEliminada(true);
		facturaRepository.save(factura);
	}

	@Transactional
	public Factura modificarFactura(Long id, Factura facturaModificada) {

		Factura facturaExistente = facturaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La factura buscada no existe."));

		if (facturaExistente.isEliminada()) {
			throw new RuntimeException("No se puede modificar una factura eliminada.");
		}

		if (facturaExistente.getEstado() == EstadoFactura.ANULADA) {
			throw new RuntimeException("No se puede modificar una factura anulada.");
		}

		if (facturaExistente.getEstado() == EstadoFactura.PAGADA) {
			throw new RuntimeException("No se puede modificar una factura pagada.");
		}

		validarDatosObligatorios(facturaModificada);

		validarTransicionEstado(facturaExistente.getEstado(), facturaModificada.getEstado());

		if (facturaModificada.getEstado() == EstadoFactura.ANULADA
				&& tieneDatosDePago(facturaModificada)) {
			throw new RuntimeException("No se pueden registrar datos de pago si la factura esta anulada.");
		}

		if (facturaModificada.getEstado() == EstadoFactura.PAGADA) {
			validarDatosDePago(facturaModificada);
		} else if (!tieneDatosDePago(facturaModificada)) {
			// Si no queda pagada, los datos de pago deben quedar vacios.
			facturaModificada.setFechaPago(null);
			facturaModificada.setMedioPago(null);
			facturaModificada.setImportePagado(null);
			facturaModificada.setInteresPagado(null);
		} else {
			throw new RuntimeException("Solo se pueden registrar datos de pago si la factura queda en estado pagada.");
		}

		EstadoFactura estadoAnterior = facturaExistente.getEstado();

		// El contrato asociado a una factura ya creada es de solo lectura.
		facturaExistente.setConcepto(facturaModificada.getConcepto());
		facturaExistente.setFechaEmision(facturaModificada.getFechaEmision());
		facturaExistente.setFechaVencimiento(facturaModificada.getFechaVencimiento());
		facturaExistente.setImporte(facturaModificada.getImporte());
		facturaExistente.setEstado(facturaModificada.getEstado());
		facturaExistente.setFechaPago(facturaModificada.getFechaPago());
		facturaExistente.setMedioPago(facturaModificada.getMedioPago());
		facturaExistente.setImportePagado(facturaModificada.getImportePagado());
		facturaExistente.setInteresPagado(facturaModificada.getInteresPagado());

		Factura facturaGuardada = facturaRepository.save(facturaExistente);

		if (estadoAnterior != facturaGuardada.getEstado()) {
			guardarHistorialEstado(facturaGuardada);
		}

		return facturaGuardada;
	}

	public List<Factura> listarFacturas(Long contratoId, Long propiedadId, Long inquilinoId, EstadoFactura estado,
										LocalDate vencimientoDesde, LocalDate vencimientoHasta) {

		List<Factura> facturas = facturaRepository.findByEliminadaFalse();
		List<Factura> facturasFiltradas = new ArrayList<>();

		for (Factura factura : facturas) {

			boolean cumpleContrato = contratoId == null
					|| (factura.getContrato() != null && contratoId.equals(factura.getContrato().getId()));

			boolean cumplePropiedad = propiedadId == null
					|| (factura.getContrato() != null && factura.getContrato().getPropiedad() != null
					&& propiedadId.equals(factura.getContrato().getPropiedad().getId()));

			boolean cumpleInquilino = inquilinoId == null
					|| (factura.getContrato() != null && factura.getContrato().getInquilino() != null
					&& inquilinoId.equals(factura.getContrato().getInquilino().getId()));

			boolean cumpleEstado = estado == null || estado.equals(factura.getEstado());

			boolean cumpleVencimiento = true;
			if (vencimientoDesde != null) {
				cumpleVencimiento = cumpleVencimiento && factura.getFechaVencimiento() != null
						&& !factura.getFechaVencimiento().isBefore(vencimientoDesde);
			}
			if (vencimientoHasta != null) {
				cumpleVencimiento = cumpleVencimiento && factura.getFechaVencimiento() != null
						&& !factura.getFechaVencimiento().isAfter(vencimientoHasta);
			}

			if (cumpleContrato && cumplePropiedad && cumpleInquilino && cumpleEstado && cumpleVencimiento) {
				facturasFiltradas.add(factura);
			}
		}

		return facturasFiltradas;
	}

	public Factura buscarPorId(Long id) {
		return facturaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La factura buscada no existe."));
	}

	public List<Contrato> encontrarContratosActivos() {
		return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
	}

	private Contrato buscarYValidarContrato(Long contratoId) {
		return contratoRepository.findById(contratoId)
				.orElseThrow(() -> new RuntimeException("El contrato indicado no existe."));
	}

	private void validarTransicionEstado(EstadoFactura actual, EstadoFactura nuevo) {

		boolean transicionValida = actual == nuevo
				|| (actual == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.PAGADA)
				|| (actual == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.VENCIDA)
				|| (actual == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.ANULADA)
				|| (actual == EstadoFactura.VENCIDA && nuevo == EstadoFactura.PAGADA);

		if (!transicionValida) {
			throw new RuntimeException(
					"No se puede cambiar el estado de la factura de " + actual + " a " + nuevo + ".");
		}
	}

	private boolean tieneDatosDePago(Factura factura) {
		return factura.getFechaPago() != null || factura.getMedioPago() != null
				|| factura.getImportePagado() != null || factura.getInteresPagado() != null;
	}

	private void validarDatosDePago(Factura factura) {

		if (factura.getFechaPago() == null) {
			throw new RuntimeException("La fecha de pago es obligatoria para marcar la factura como pagada.");
		}

		if (factura.getMedioPago() == null) {
			throw new RuntimeException("El medio de pago es obligatorio para marcar la factura como pagada.");
		}

		if (factura.getImportePagado() == null) {
			throw new RuntimeException("El importe pagado es obligatorio para marcar la factura como pagada.");
		} else if (factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El importe pagado debe ser un numero positivo.");
		}
	}

	private void validarDatosObligatorios(Factura factura) {

		if (factura.getContrato() == null || factura.getContrato().getId() == null) {
			throw new RuntimeException("El contrato es obligatorio.");
		}

		if (factura.getConcepto() == null || factura.getConcepto().trim().isEmpty()) {
			throw new RuntimeException("El concepto facturado es obligatorio.");
		}

		if (factura.getFechaEmision() == null) {
			throw new RuntimeException("La fecha de emision es obligatoria.");
		}

		if (factura.getFechaVencimiento() == null) {
			throw new RuntimeException("La fecha de vencimiento es obligatoria.");
		}

		if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
			throw new RuntimeException("La fecha de vencimiento debe ser igual o posterior a la fecha de emision.");
		}

		if (factura.getImporte() == null) {
			throw new RuntimeException("El importe es obligatorio.");
		} else if (factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El importe debe ser un numero positivo.");
		}
	}

	private void guardarHistorialEstado(Factura factura) {

		HistorialEstadoFactura historial = new HistorialEstadoFactura();
		historial.setFactura(factura);
		historial.setEstado(factura.getEstado());
		historial.setFechaHora(LocalDateTime.now());

		historialEstadoFacturaRepository.save(historial);
	}
}