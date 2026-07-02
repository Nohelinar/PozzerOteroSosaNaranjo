package com.tuti.desi.pozzeroterososanaranjo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.HistorialEstadoPublicacion;
import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.entity.Publicacion;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPublicacion;
import com.tuti.desi.pozzeroterososanaranjo.repository.HistorialEstadoPublicacionRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PropiedadRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PublicacionRepository;

@Service
public class PublicacionService {

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private HistorialEstadoPublicacionRepository historialEstadoPublicacionRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Transactional
	public Publicacion altaPublicacion(Publicacion publicacion) {

		validarPublicacion(publicacion);

		Propiedad propiedad = buscarYValidarPropiedad(publicacion.getPropiedad().getId());

		if (Boolean.TRUE.equals(propiedad.getEliminada())) {
			throw new RuntimeException("La propiedad indicada esta eliminada.");
		}

		if (propiedad.getEstadoPropiedad() != EstadoPropiedad.DISPONIBLE) {
			throw new RuntimeException("Solo se puede publicar una propiedad que se encuentre disponible.");
		}

		if (publicacionRepository.existsByPropiedadIdAndEstadoAndEliminadaFalse(propiedad.getId(),
				EstadoPublicacion.ACTIVA)) {
			throw new RuntimeException("Ya existe una publicacion activa para esta propiedad.");
		}

		publicacion.setPropiedad(propiedad);

		if (publicacion.getEstado() == null) {
			publicacion.setEstado(EstadoPublicacion.ACTIVA);
		}

		publicacion.setEliminada(false);

		Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

		guardarHistorialEstado(publicacionGuardada);

		return publicacionGuardada;
	}

	@Transactional
	public void eliminarPublicacion(Long id) {

		Publicacion publicacion = publicacionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La publicacion buscada no existe."));

		if (publicacion.isEliminada()) {
			throw new RuntimeException("La publicacion ya se encuentra eliminada.");
		}

		if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
			throw new RuntimeException(
					"Solo se pueden eliminar publicaciones activas. Si ya no corresponde ofrecerla, pausela o finalicela.");
		}

		publicacion.setEliminada(true);
		publicacionRepository.save(publicacion);
	}

	@Transactional
	public Publicacion modificarPublicacion(Long id, Publicacion publicacionModificada) {

		Publicacion publicacionExistente = publicacionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La publicacion buscada no existe."));

		if (publicacionExistente.isEliminada()) {
			throw new RuntimeException("No se puede modificar una publicacion eliminada.");
		}

		validarDatosBasicos(publicacionModificada);

		if (publicacionModificada.getEstado() == EstadoPublicacion.ACTIVA
				&& publicacionExistente.getEstado() != EstadoPublicacion.ACTIVA) {

			Propiedad propiedad = publicacionExistente.getPropiedad();

			if (propiedad.getEstadoPropiedad() != EstadoPropiedad.DISPONIBLE) {
				throw new RuntimeException("Solo se puede activar la publicacion si la propiedad esta disponible.");
			}

			if (publicacionRepository.existsByPropiedadIdAndEstadoAndEliminadaFalse(propiedad.getId(),
					EstadoPublicacion.ACTIVA)) {
				throw new RuntimeException("Ya existe una publicacion activa para esta propiedad.");
			}
		}

		if (publicacionExistente.getEstado() == EstadoPublicacion.FINALIZADA
				&& !publicacionModificada.getCondicionesAlquiler().equals(publicacionExistente.getCondicionesAlquiler())) {
			throw new RuntimeException("No se pueden modificar las condiciones de alquiler de una publicacion finalizada.");
		}

		EstadoPublicacion estadoAnterior = publicacionExistente.getEstado();

		// La propiedad asociada es de solo lectura una vez creada la publicacion.
		publicacionExistente.setPrecioMensual(publicacionModificada.getPrecioMensual());
		publicacionExistente.setCondicionesAlquiler(publicacionModificada.getCondicionesAlquiler());
		publicacionExistente.setDescripcion(publicacionModificada.getDescripcion());
		publicacionExistente.setFechaPublicacion(publicacionModificada.getFechaPublicacion());
		publicacionExistente.setEstado(publicacionModificada.getEstado());

		Publicacion publicacionGuardada = publicacionRepository.save(publicacionExistente);

		if (estadoAnterior != publicacionGuardada.getEstado()) {
			guardarHistorialEstado(publicacionGuardada);
		}

		return publicacionGuardada;
	}

	public List<Publicacion> listarPublicaciones(Long propiedadId, String ciudad, EstadoPublicacion estado,
												 BigDecimal precioDesde, BigDecimal precioHasta) {

		List<Publicacion> publicaciones = publicacionRepository.findByEliminadaFalse();
		List<Publicacion> publicacionesFiltradas = new java.util.ArrayList<>();

		for (Publicacion publicacion : publicaciones) {

			boolean cumplePropiedad = propiedadId == null
					|| (publicacion.getPropiedad() != null && propiedadId.equals(publicacion.getPropiedad().getId()));

			boolean cumpleCiudad = ciudad == null || ciudad.trim().isEmpty()
					|| (publicacion.getPropiedad() != null && publicacion.getPropiedad().getCiudad() != null
					&& publicacion.getPropiedad().getCiudad().toLowerCase()
					.contains(ciudad.toLowerCase().trim()));

			boolean cumpleEstado = estado == null || estado.equals(publicacion.getEstado());

			boolean cumplePrecio = true;
			if (precioDesde != null) {
				cumplePrecio = cumplePrecio && publicacion.getPrecioMensual() != null
						&& publicacion.getPrecioMensual().compareTo(precioDesde) >= 0;
			}
			if (precioHasta != null) {
				cumplePrecio = cumplePrecio && publicacion.getPrecioMensual() != null
						&& publicacion.getPrecioMensual().compareTo(precioHasta) <= 0;
			}

			if (cumplePropiedad && cumpleCiudad && cumpleEstado && cumplePrecio) {
				publicacionesFiltradas.add(publicacion);
			}
		}

		return publicacionesFiltradas;
	}

	public Publicacion buscarPorId(Long id) {
		return publicacionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("La publicacion buscada no existe."));
	}

	public List<Propiedad> encontrarPropiedadesDisponibles() {
		return propiedadRepository.findByEstadoPropiedadAndEliminadaFalse(EstadoPropiedad.DISPONIBLE);
	}

	private Propiedad buscarYValidarPropiedad(Long propiedadId) {
		return propiedadRepository.findById(propiedadId)
				.orElseThrow(() -> new RuntimeException("La propiedad indicada no existe."));
	}

	private void validarDatosBasicos(Publicacion publicacion) {

		if (publicacion.getPrecioMensual() == null) {
			throw new RuntimeException("El precio mensual es obligatorio.");
		} else if (publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El precio mensual debe ser positivo.");
		}

		if (publicacion.getCondicionesAlquiler() == null || publicacion.getCondicionesAlquiler().trim().isEmpty()) {
			throw new RuntimeException("Las condiciones de alquiler son obligatorias.");
		}

		if (publicacion.getDescripcion() == null || publicacion.getDescripcion().trim().isEmpty()) {
			throw new RuntimeException("La descripcion es obligatoria.");
		}

		if (publicacion.getFechaPublicacion() == null) {
			throw new RuntimeException("La fecha de publicacion es obligatoria.");
		}

		if (publicacion.getEstado() == null) {
			throw new RuntimeException("El estado es obligatorio.");
		}
	}

	private void validarPublicacion(Publicacion publicacion) {

		if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
			throw new RuntimeException("La propiedad es obligatoria.");
		}

		validarDatosBasicos(publicacion);
	}

	private void guardarHistorialEstado(Publicacion publicacion) {

		HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
		historial.setPublicacion(publicacion);
		historial.setEstado(publicacion.getEstado());
		historial.setFechaHora(LocalDateTime.now());

		historialEstadoPublicacionRepository.save(historial);
	}
}