package com.tuti.desi.pozzeroterososanaranjo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Publicacion;
import com.tuti.desi.pozzeroterososanaranjo.entity.Visita;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoVisita;
import com.tuti.desi.pozzeroterososanaranjo.repository.PublicacionRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.VisitaRepository;

@Service
public class VisitaService {

	@Autowired
	private VisitaRepository visitaRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Transactional
	public Visita altaVisita(Visita visita) {

		if (visita.getPublicacion() == null || visita.getPublicacion().getId() == null) {
			throw new RuntimeException("La publicacion es obligatoria.");
		}

		Publicacion publicacion = publicacionRepository.findById(visita.getPublicacion().getId())
				.orElseThrow(() -> new RuntimeException("La publicacion indicada no existe."));

		if (publicacion.isEliminada()) {
			throw new RuntimeException("La publicacion indicada esta eliminada.");
		}

		visita.setPublicacion(publicacion);

		if (visita.getFechaHora() == null) {
			throw new RuntimeException("La fecha y hora de la visita son obligatorias.");
		}

		if (visita.getEstado() == null) {
			visita.setEstado(EstadoVisita.PENDIENTE);
		}

		return visitaRepository.save(visita);
	}

	@Transactional
	public Visita modificarVisita(Long id, Visita visitaModificada) {

		Visita visitaExistente = visitaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La visita buscada no existe."));

		if (visitaModificada.getFechaHora() == null) {
			throw new RuntimeException("La fecha y hora de la visita son obligatorias.");
		}

		if (visitaModificada.getEstado() == null) {
			throw new RuntimeException("El estado es obligatorio.");
		}

		// La publicacion asociada es de solo lectura una vez creada la visita.
		visitaExistente.setFechaHora(visitaModificada.getFechaHora());
		visitaExistente.setEstado(visitaModificada.getEstado());

		return visitaRepository.save(visitaExistente);
	}

	public List<Visita> listarPorPublicacion(Long publicacionId) {

		if (publicacionId != null) {
			return visitaRepository.findByPublicacionIdOrderByFechaHoraDesc(publicacionId);
		}

		return visitaRepository.findAllByOrderByFechaHoraDesc();
	}

	public Visita buscarPorId(Long id) {
		return visitaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La visita buscada no existe."));
	}

	public List<Publicacion> encontrarPublicaciones() {
		return publicacionRepository.findByEliminadaFalse();
	}
}
