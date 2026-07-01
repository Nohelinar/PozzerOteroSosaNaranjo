
package com.tuti.desi.PozzerOteroSosaNaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

	boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

	boolean existsByDireccionAndCiudadAndEliminadaFalseAndIdNot(String direccion, String ciudad, Long id);

	List<Propiedad> findByEliminadaFalse();

}