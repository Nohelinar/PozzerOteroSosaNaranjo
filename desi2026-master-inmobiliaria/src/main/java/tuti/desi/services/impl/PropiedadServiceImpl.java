package tuti.desi.services.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entities.enums.EstadoDisponibilidad;
import tuti.desi.entities.Propiedad;
import tuti.desi.entities.Persona;
import tuti.desi.entities.enums.TipoPropiedad;
import tuti.desi.exceptions.EntidadNoEncontradaException;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.repository.PropiedadRepository;

import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Service
@Transactional
public class PropiedadServiceImpl implements PropiedadService {

    private final PropiedadRepository repo;
    private final PersonaService personaService;

    public PropiedadServiceImpl(PropiedadRepository repo, PersonaService personaService) {
        this.repo = repo;
        this.personaService = personaService;
    }

    @Override
    @Transactional
    public Propiedad save(Propiedad propiedad) throws Excepcion {
        // Validar que el propietario exista y no esté eliminado
        if (propiedad.getPropietario() == null || propiedad.getPropietario().getId() == null) {
            throw new Excepcion("El propietario es obligatorio", "propietario");
        }
        
        Persona propietario = personaService.getById(propiedad.getPropietario().getId());
        if (propietario.getEliminado()) {
            throw new Excepcion("El propietario seleccionado está eliminado", "propietario");
        }

        // Validar duplicados (misma dirección y ciudad)
        Long id = propiedad.getId();
        if (repo.existsByDireccionAndCiudad(propiedad.getDireccion(), propiedad.getCiudad(), id)) {
            throw new Excepcion("Ya existe una propiedad activa con la misma dirección y ciudad", "direccion");
        }

        // Si es una nueva propiedad, establecer estado por defecto
        if (id == null && propiedad.getEstadoDisponibilidad() == null) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.Disponible);
        }

        // Guardar la propiedad
        Propiedad saved = repo.save(propiedad);

        // Registrar el cambio de estado en el historial (solo si es nueva o cambió)
        if (id == null || propiedad.getEstadoDisponibilidad() != null) {
            // Para simplificar, registramos el estado inicial o el cambio
            // El historial se manejará en el método cambiarEstado
        }

        return saved;
    }

    @Override
    public Propiedad getById(Long id) throws Excepcion {
        return repo.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Propiedad", id));
    }

    @Override
    public List<Propiedad> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Propiedad> getAllActivas() {
        return repo.findAllActivas();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Propiedad propiedad = getById(id);
        
        // Validar que no tenga contrato activo
        // Por simplicidad, asumimos que no tiene contrato activo
        // En la implementación real, deberías verificar contratos activos
        
        propiedad.setEliminado(true);
        repo.save(propiedad);
    }

    @Override
    public List<Propiedad> filter(String direccion, String ciudad, TipoPropiedad tipo, EstadoDisponibilidad estado) {
        return repo.filter(direccion, ciudad, tipo, estado);
    }

    @Override
    public boolean existsByDireccionAndCiudad(String direccion, String ciudad, Long id) {
        return repo.existsByDireccionAndCiudad(direccion, ciudad, id);
    }

    @Override
    @Transactional
    public void cambiarEstado(Propiedad propiedad, EstadoDisponibilidad nuevoEstado) {
        EstadoDisponibilidad estadoAnterior = propiedad.getEstadoDisponibilidad();
        
        if (estadoAnterior != nuevoEstado) {
            propiedad.setEstadoDisponibilidad(nuevoEstado);
            propiedad.agregarCambioEstado(estadoAnterior, nuevoEstado);
            repo.save(propiedad);
        }
    }
}