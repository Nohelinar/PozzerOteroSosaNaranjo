package com.tuti.desi.PozzerOteroSosaNaranjo.service;

import com.tuti.desi.PozzerOteroSosaNaranjo.repository.PersonaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Contrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.entity.HistorialEstadoContrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Persona;
import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Propiedad;
import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoContrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoPropiedad;
import com.tuti.desi.PozzerOteroSosaNaranjo.repository.ContratoRepository;
import com.tuti.desi.PozzerOteroSosaNaranjo.repository.HistorialEstadoContratoRepository;
import com.tuti.desi.PozzerOteroSosaNaranjo.repository.PropiedadRepository;

@Service
public class ContratoService {

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private HistorialEstadoContratoRepository historialEstadoContratoRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private PersonaRepository personaRepository;

	public Contrato altaContrato(Contrato contrato) {

		validarContrato(contrato);

		Long propiedadId = contrato.getPropiedad().getId();

		List<Contrato> contratos = contratoRepository.findByPropiedadIdAndEstado(propiedadId, EstadoContrato.ACTIVO);

		if (!contratos.isEmpty()) {
			throw new RuntimeException("Ya existe un contrato activo para esta propiedad.");
		}

		if (contrato.getEstado() == EstadoContrato.ACTIVO
				&& contrato.getPropiedad().getEstadoPropiedad() != EstadoPropiedad.DISPONIBLE) {
			throw new RuntimeException("No se puede crear un contrato activo, la propiedad no esta disponible.");
		}

		if (contrato.getEstado() == EstadoContrato.ACTIVO) {
			contrato.getPropiedad().setEstadoPropiedad(EstadoPropiedad.ALQUILADA);
			propiedadRepository.save(contrato.getPropiedad());
		}

		Contrato contratoGuardado = contratoRepository.save(contrato);

		guardarHistorialEstadoContrato(contratoGuardado);

		return contratoGuardado;
	}

	public void eliminarContrato(Long id) {

		Contrato contrato = contratoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El contrato buscado no existe."));

		if (contrato.isEliminado()) {
			throw new RuntimeException("El contrato ya se elimino");
		}
		if (contrato.getEstado() != EstadoContrato.BORRADOR) {
			throw new RuntimeException("El contrato no se puede eliminar debido a que no esta en borrador.");
		}

		contrato.setEliminado(true);
		contratoRepository.save(contrato);
	}

	public Contrato modificarContrato(Long id, Contrato contratoModificado) {

		Contrato contratoExistente = contratoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El contrato buscado no existe."));

		validarContrato(contratoModificado);

		if ((contratoExistente.getEstado() == EstadoContrato.FINALIZADO
				|| contratoExistente.getEstado() == EstadoContrato.RESCINDIDO
				|| contratoExistente.getEstado() == EstadoContrato.ACTIVO)
				&& contratoModificado.getEstado() == EstadoContrato.BORRADOR) {
			throw new RuntimeException(
					"No se puede actualizar el estado del contrato de finalizado, rescindido o activo a borrador.");
		}

		if ((contratoExistente.getEstado() == EstadoContrato.FINALIZADO
				|| contratoExistente.getEstado() == EstadoContrato.RESCINDIDO)
				&& contratoModificado.getEstado() == EstadoContrato.ACTIVO) {

			throw new RuntimeException(
					"No se puede actualizar el estado del contrato de finalizado o rescindido a activo.");
		}

		if ((contratoExistente.getEstado() == EstadoContrato.BORRADOR
				|| contratoExistente.getEstado() == EstadoContrato.RESCINDIDO)
				&& contratoModificado.getEstado() == EstadoContrato.FINALIZADO) {
			throw new RuntimeException(
					"No se puede actualizar el estado del contrato de borrador o rescindido a finalizado.");
		}

		if ((contratoExistente.getEstado() == EstadoContrato.BORRADOR
				|| contratoExistente.getEstado() == EstadoContrato.FINALIZADO)
				&& contratoModificado.getEstado() == EstadoContrato.RESCINDIDO) {
			throw new RuntimeException(
					"No se puede actualizar el estado del contrato de borrador o finalizado a rescindido.");
		}

		if (contratoModificado.getEstado() == EstadoContrato.FINALIZADO
				|| contratoModificado.getEstado() == EstadoContrato.RESCINDIDO) {
			contratoExistente.getPropiedad().setEstadoPropiedad(EstadoPropiedad.DISPONIBLE);
			propiedadRepository.save(contratoExistente.getPropiedad());
		}

		if (contratoExistente.getEstado() != EstadoContrato.ACTIVO) {
			if (contratoModificado.getEstado() == EstadoContrato.ACTIVO
					&& contratoModificado.getPropiedad().getEstadoPropiedad() != EstadoPropiedad.DISPONIBLE) {
				throw new RuntimeException("No se puede activar el contrato, la propiedad no esta disponible.");
			}
		}

		if (contratoModificado.getEstado() == EstadoContrato.ACTIVO) {

			Long propiedadId = contratoExistente.getPropiedad().getId();

			List<Contrato> contratos = contratoRepository.findByPropiedadIdAndEstado(propiedadId,
					EstadoContrato.ACTIVO);

			for (Contrato c : contratos) {
				if (!c.getId().equals(contratoExistente.getId())) {
					throw new RuntimeException("Ya existe un contrato activo para esta propiedad.");
				}
			}
			contratoModificado.getPropiedad().setEstadoPropiedad(EstadoPropiedad.ALQUILADA);
			propiedadRepository.save(contratoModificado.getPropiedad());
		}

		contratoExistente.setPropiedad(contratoModificado.getPropiedad());
		contratoExistente.setInquilino(contratoModificado.getInquilino());
		contratoExistente.setFechaInicio(contratoModificado.getFechaInicio());
		contratoExistente.setDuracionMeses(contratoModificado.getDuracionMeses());
		contratoExistente.setImporteMensual(contratoModificado.getImporteMensual());
		contratoExistente.setDiaVencimientoMensual(contratoModificado.getDiaVencimientoMensual());
		contratoExistente.setDescripcion(contratoModificado.getDescripcion());
		contratoExistente.setEstado(contratoModificado.getEstado());

		Contrato contratoGuardado = contratoRepository.save(contratoExistente);

		guardarHistorialEstadoContrato(contratoGuardado);

		return contratoGuardado;

	}

	public List<Contrato> listarContratos(Long propiedadId, Long inquilinoId, EstadoContrato estado,
			LocalDate fechaDesde, LocalDate fechaHasta) {

		if (propiedadId != null) {
			return contratoRepository.findByPropiedadIdAndEliminadoFalse(propiedadId);
		}

		if (inquilinoId != null) {
			return contratoRepository.findByInquilinoIdAndEliminadoFalse(inquilinoId);
		}

		if (estado != null) {
			return contratoRepository.findByEstadoAndEliminadoFalse(estado);
		}

		if (fechaDesde != null && fechaHasta != null) {
			return contratoRepository.findByFechaInicioBetweenAndEliminadoFalse(fechaDesde, fechaHasta);
		}

		return contratoRepository.findByEliminadoFalse();
	}

	public void validarContrato(Contrato contrato) {

		if (contrato.getPropiedad() == null || contrato.getPropiedad().getId() == null) {
			throw new RuntimeException("La propiedad es obligatoria.");
		}

		if (contrato.getInquilino() == null || contrato.getInquilino().getId() == null) {
			throw new RuntimeException("El inquilino es obligatorio.");
		}

		if (contrato.getFechaInicio() == null) {
			throw new RuntimeException("La fecha de inicio es obligatoria.");
		}

		if (contrato.getDuracionMeses() == null) {
			throw new RuntimeException("La duracion en meses es obligatoria.");
		} else if (contrato.getDuracionMeses() <= 0) {
			throw new RuntimeException("La duracion en meses debe ser positiva.");
		}

		if (contrato.getImporteMensual() == null) {
			throw new RuntimeException("El importe mensual es obligatorio.");
		} else if (contrato.getImporteMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El importe mensual debe ser positivo.");
		}

		if (contrato.getDiaVencimientoMensual() == null) {
			throw new RuntimeException("El dia de vencimiento mensual es obligatorio.");
		} else if (contrato.getDiaVencimientoMensual() <= 0 || contrato.getDiaVencimientoMensual() > 31) {
			throw new RuntimeException("El dia de vencimiento mensual debe ser un numero entre 1 y 31.");
		}

		if (contrato.getDescripcion() == null || contrato.getDescripcion().trim().isEmpty()) {
			throw new RuntimeException("La descripcion es obligatoria.");
		}

		if (contrato.getEstado() == null) {
			throw new RuntimeException("El estado es obligatorio.");
		}
	}

	public Contrato buscarPorId(Long id) {

		Contrato contrato = contratoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El contrato buscado no existe."));

		return contrato;
	}

	private void guardarHistorialEstadoContrato(Contrato contrato) {

		HistorialEstadoContrato historial = new HistorialEstadoContrato();
		historial.setContrato(contrato);
		historial.setEstado(contrato.getEstado());
		historial.setFechaHora(LocalDateTime.now());

		historialEstadoContratoRepository.save(historial);
	}

	public List<Propiedad> encontrarPropiedades() {
		return propiedadRepository.findByEliminadaFalse();
	}

	public List<Persona> encontrarPersonas() {
		return personaRepository.findByEliminadaFalse();
	}
}
