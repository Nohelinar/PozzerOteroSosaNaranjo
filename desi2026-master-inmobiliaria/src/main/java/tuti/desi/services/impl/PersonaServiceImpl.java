package tuti.desi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tuti.desi.entities.Persona;
import tuti.desi.repository.PersonaRepository;
import tuti.desi.services.PersonaService;

@Transactional
@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository repo;

    PersonaServiceImpl(PersonaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Persona save(Persona persona) {
        return repo.save(persona);
    }

    @Override
    public Persona getById(Long id) {
        return repo.findById(id)
            .orElseThrow();
    }

    @Override
    public List<Persona> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Persona> getAllActivas() {
        return repo.findAllActivas();
    }

    @Override
    public void deleteById(Long id) {
        Persona persona = getById(id);
        persona.setEliminado(true);
        repo.save(persona);
    }

    @Override
    public List<Persona> filter(String nombre, String apellido, String email) {
        return repo.filter(nombre, apellido, email);
    }
}