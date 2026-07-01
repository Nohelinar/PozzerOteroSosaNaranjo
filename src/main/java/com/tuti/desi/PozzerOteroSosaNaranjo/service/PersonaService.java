
package com.tuti.desi.PozzerOteroSosaNaranjo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Persona;
import com.tuti.desi.PozzerOteroSosaNaranjo.repository.PersonaRepository;

@Service
public class PersonaService {

	@Autowired
	private PersonaRepository personaRepository;

	public List<Persona> listarNoEliminadas() {
		return personaRepository.findByEliminadaFalse();
	}

	public Persona guardar(Persona persona) {

		if (persona.getEliminada() == null) {
			persona.setEliminada(false);
		}

		return personaRepository.save(persona);
	}
}