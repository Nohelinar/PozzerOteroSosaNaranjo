package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

    boolean existsByDireccionAndCiudadAndEliminadaFalseAndIdNot(String direccion, String ciudad, Long id);

    List<Propiedad> findByEliminadaFalse();

    List<Propiedad> findByEstadoPropiedadAndEliminadaFalse(EstadoPropiedad estadoPropiedad);

    boolean existsByPropietarioIdAndEliminadaFalse(Long propietarioId);

}