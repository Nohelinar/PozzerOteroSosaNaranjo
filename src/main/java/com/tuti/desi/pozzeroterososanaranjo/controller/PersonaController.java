
package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.service.PersonaService;

@RestController
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/personas/no-eliminadas")
    public List<Persona> listarPersonasNoEliminadas() {
        return personaService.listarNoEliminadas();
    }

    @PostMapping("/personas")
    public Persona crearPersona(@RequestBody Persona persona) {
        return personaService.guardar(persona);
    }
}