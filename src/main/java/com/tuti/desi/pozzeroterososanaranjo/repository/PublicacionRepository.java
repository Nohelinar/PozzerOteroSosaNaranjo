package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Publicacion;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPublicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

	List<Publicacion> findByEliminadaFalse();

	List<Publicacion> findByPropiedadIdAndEliminadaFalse(Long propiedadId);

	List<Publicacion> findByPropiedadCiudadAndEliminadaFalse(String ciudad);

	List<Publicacion> findByEstadoAndEliminadaFalse(EstadoPublicacion estado);

	List<Publicacion> findByPrecioMensualBetweenAndEliminadaFalse(BigDecimal desde, BigDecimal hasta);

	List<Publicacion> findByPropiedadIdAndEstado(Long propiedadId, EstadoPublicacion estado);

	boolean existsByPropiedadIdAndEstadoAndEliminadaFalse(Long propiedadId, EstadoPublicacion estado);
}
