package com.tuti.desi.pozzeroterososanaranjo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.HistorialEstadoIncidente;
import com.tuti.desi.pozzeroterososanaranjo.entity.Incidente;
import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoIncidente;
import com.tuti.desi.pozzeroterososanaranjo.repository.HistorialEstadoIncidenteRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.IncidenteRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PropiedadRepository;

@Service
public class IncidenteService {

	@Autowired
	private IncidenteRepository incidenteRepository;

	@Autowired
	private HistorialEstadoIncidenteRepository historialEstadoIncidenteRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Transactional
	public Incidente altaIncidente(Incidente incidente) {

		if (incidente.getPropiedad() == null || incidente.getPropiedad().getId() == null) {
			throw new RuntimeException("La propiedad es obligatoria.");
		}

		Propiedad propiedad = propiedadRepository.findById(incidente.getPropiedad().getId())
				.orElseThrow(() -> new RuntimeException("La propiedad indicada no existe."));

		if (Boolean.TRUE.equals(propiedad.getEliminada())) {
			throw new RuntimeException("La propiedad indicada esta eliminada.");
		}

		incidente.setPropiedad(propiedad);

		if (incidente.getEstado() == null) {
			incidente.setEstado(EstadoIncidente.ABIERTO);
		}

		if (incidente.getFechaAlta() == null) {
			incidente.setFechaAlta(LocalDateTime.now());
		}

		validarDatosBasicos(incidente);

		incidente.setEliminado(false);

		Incidente incidenteGuardado = incidenteRepository.save(incidente);

		guardarHistorialEstado(incidenteGuardado);

		return incidenteGuardado;
	}

	@Transactional
	public Incidente modificarIncidente(Long id, Incidente incidenteModificado) {

		Incidente incidenteExistente = incidenteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El incidente buscado no existe."));

		if (incidenteExistente.isEliminado()) {
			throw new RuntimeException("No se puede modificar un incidente eliminado.");
		}

		validarDatosBasicos(incidenteModificado);

		EstadoIncidente estadoAnterior = incidenteExistente.getEstado();

		// La propiedad asociada es de solo lectura una vez creado el incidente.
		incidenteExistente.setTitulo(incidenteModificado.getTitulo());
		incidenteExistente.setDescripcion(incidenteModificado.getDescripcion());
		incidenteExistente.setCategoria(incidenteModificado.getCategoria());
		incidenteExistente.setPrioridad(incidenteModificado.getPrioridad());
		incidenteExistente.setEstado(incidenteModificado.getEstado());
		incidenteExistente.setFechaResolucion(incidenteModificado.getFechaResolucion());
		incidenteExistente.setObservacionesResolucion(incidenteModificado.getObservacionesResolucion());
		incidenteExistente.setCostoResolucion(incidenteModificado.getCostoResolucion());
		incidenteExistente.setResponsableTecnico(incidenteModificado.getResponsableTecnico());

		Incidente incidenteGuardado = incidenteRepository.save(incidenteExistente);

		if (estadoAnterior != incidenteGuardado.getEstado()) {
			guardarHistorialEstado(incidenteGuardado);
		}

		return incidenteGuardado;
	}

	@Transactional
	public void eliminarIncidente(Long id) {

		Incidente incidente = incidenteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El incidente buscado no existe."));

		if (incidente.isEliminado()) {
			throw new RuntimeException("El incidente ya se encuentra eliminado.");
		}

		incidente.setEliminado(true);
		incidenteRepository.save(incidente);
	}

	public List<Incidente> listarConFiltros(Long propiedadId, EstadoIncidente estado) {

		List<Incidente> incidentes = incidenteRepository.findByEliminadoFalseOrderByFechaAltaDesc();
		List<Incidente> incidentesFiltrados = new ArrayList<>();

		for (Incidente incidente : incidentes) {

			boolean cumplePropiedad = propiedadId == null
					|| (incidente.getPropiedad() != null && propiedadId.equals(incidente.getPropiedad().getId()));

			boolean cumpleEstado = estado == null || estado.equals(incidente.getEstado());

			if (cumplePropiedad && cumpleEstado) {
				incidentesFiltrados.add(incidente);
			}
		}

		return incidentesFiltrados;
	}

	public Incidente buscarPorId(Long id) {

		Incidente incidente = incidenteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("El incidente buscado no existe."));

		if (incidente.isEliminado()) {
			throw new RuntimeException("El incidente indicado esta eliminado.");
		}

		return incidente;
	}

	public List<Propiedad> encontrarPropiedades() {
		return propiedadRepository.findByEliminadaFalse();
	}

	private void validarDatosBasicos(Incidente incidente) {

		if (incidente.getTitulo() == null || incidente.getTitulo().trim().isEmpty()) {
			throw new RuntimeException("El titulo es obligatorio.");
		}

		if (incidente.getDescripcion() == null || incidente.getDescripcion().trim().isEmpty()) {
			throw new RuntimeException("La descripcion es obligatoria.");
		}

		if (incidente.getCategoria() == null) {
			throw new RuntimeException("La categoria es obligatoria.");
		}

		if (incidente.getPrioridad() == null) {
			throw new RuntimeException("La prioridad es obligatoria.");
		}

		if (incidente.getEstado() == null) {
			throw new RuntimeException("El estado es obligatorio.");
		}

		if (incidente.getCostoResolucion() != null
				&& incidente.getCostoResolucion().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("El costo de resolucion no puede ser negativo.");
		}
	}

	private void guardarHistorialEstado(Incidente incidente) {

		HistorialEstadoIncidente historial = new HistorialEstadoIncidente();
		historial.setIncidente(incidente);
		historial.setEstado(incidente.getEstado());
		historial.setFechaHora(LocalDateTime.now());

		historialEstadoIncidenteRepository.save(historial);
	}
}
