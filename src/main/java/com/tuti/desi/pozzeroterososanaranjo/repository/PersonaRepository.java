
package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadaFalse();

    List<Persona> findByEliminadaFalseOrderByApellidoAscNombreAsc();

}
