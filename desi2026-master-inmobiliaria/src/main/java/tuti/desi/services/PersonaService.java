package tuti.desi.services;

import java.util.List;

import tuti.desi.entities.Persona;
import tuti.desi.exceptions.Excepcion;

public interface PersonaService {
    Persona save(Persona persona) throws Excepcion;
    Persona getById(Long id) throws Excepcion;
    List<Persona> getAll();
    List<Persona> getAllActivas();
    void deleteById(Long id) throws Excepcion;
    List<Persona> filter(String nombre, String apellido, String email);
}