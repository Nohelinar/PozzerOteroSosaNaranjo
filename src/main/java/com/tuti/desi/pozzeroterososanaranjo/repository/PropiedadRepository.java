
package com.tuti.desi.pozzeroterososanaranjo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

}