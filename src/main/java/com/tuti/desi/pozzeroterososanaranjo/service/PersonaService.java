
package com.tuti.desi.pozzeroterososanaranjo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.repository.ContratoRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PersonaRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PropiedadRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Persona> listarNoEliminadas() {
        return personaRepository.findByEliminadaFalseOrderByApellidoAscNombreAsc();
    }

    public List<Persona> listarConFiltros(String nombre, String apellido) {

        List<Persona> personas = personaRepository.findByEliminadaFalseOrderByApellidoAscNombreAsc();
        List<Persona> personasFiltradas = new ArrayList<>();

        for (Persona persona : personas) {

            boolean cumpleNombre = nombre == null || nombre.trim().isEmpty()
                    || contieneTexto(persona.getNombre(), nombre);

            boolean cumpleApellido = apellido == null || apellido.trim().isEmpty()
                    || contieneTexto(persona.getApellido(), apellido);

            if (cumpleNombre && cumpleApellido) {
                personasFiltradas.add(persona);
            }
        }

        return personasFiltradas;
    }

    public Persona buscarPorId(Long id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La persona indicada no existe."));

        if (Boolean.TRUE.equals(persona.getEliminada())) {
            throw new RuntimeException("La persona indicada esta eliminada.");
        }

        return persona;
    }

    @Transactional
    public Persona guardar(Persona persona) {

        validarPersona(persona);

        if (persona.getEliminada() == null) {
            persona.setEliminada(false);
        }

        return personaRepository.save(persona);
    }

    @Transactional
    public Persona modificar(Long id, Persona personaActualizada) {

        Persona personaExistente = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La persona indicada no existe."));

        if (Boolean.TRUE.equals(personaExistente.getEliminada())) {
            throw new RuntimeException("No se puede modificar una persona eliminada.");
        }

        validarPersona(personaActualizada);

        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setApellido(personaActualizada.getApellido());

        return personaRepository.save(personaExistente);
    }

    @Transactional
    public void eliminarLogicamente(Long id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La persona indicada no existe."));

        if (Boolean.TRUE.equals(persona.getEliminada())) {
            throw new RuntimeException("La persona ya se encuentra eliminada.");
        }

        if (propiedadRepository.existsByPropietarioIdAndEliminadaFalse(id)) {
            throw new RuntimeException("No se puede eliminar la persona porque es propietaria de una propiedad activa.");
        }

        if (!contratoRepository.findByInquilinoIdAndEliminadoFalse(id).isEmpty()) {
            throw new RuntimeException("No se puede eliminar la persona porque tiene contratos activos como inquilino.");
        }

        persona.setEliminada(true);
        personaRepository.save(persona);
    }

    private void validarPersona(Persona persona) {

        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }

        if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio.");
        }
    }

    private boolean contieneTexto(String textoCompleto, String textoBuscado) {

        if (textoCompleto == null) {
            return false;
        }

        return textoCompleto.toLowerCase().contains(textoBuscado.toLowerCase().trim());
    }
}
