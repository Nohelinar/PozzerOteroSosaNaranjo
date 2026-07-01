
package com.tuti.desi.PozzerOteroSosaNaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

	List<Persona> findByEliminadaFalse();

}