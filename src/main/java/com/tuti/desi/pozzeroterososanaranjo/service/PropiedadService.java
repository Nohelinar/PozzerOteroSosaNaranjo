
package com.tuti.desi.pozzeroterososanaranjo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.HistorialEstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.repository.HistorialEstadoPropiedadRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PersonaRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.PropiedadRepository;

@Service
public class PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private HistorialEstadoPropiedadRepository historialEstadoPropiedadRepository;

    public List<Propiedad> listarTodas() {
        return propiedadRepository.findAll();
    }

    @Transactional
    public Propiedad guardar(Propiedad propiedad) {

        boolean esNueva = propiedad.getId() == null;

        validarPropiedad(propiedad);

        Persona propietario = buscarYValidarPropietario(propiedad);
        propiedad.setPropietario(propietario);

        if (propiedad.getEstadoPropiedad() == null) {
            propiedad.setEstadoPropiedad(EstadoPropiedad.DISPONIBLE);
        }

        if (propiedad.getEliminada() == null) {
            propiedad.setEliminada(false);
        }

        if (esNueva &&
            propiedadRepository.existsByDireccionAndCiudadAndEliminadaFalse(
                propiedad.getDireccion(),
                propiedad.getCiudad()
            )) {
            throw new RuntimeException("Ya existe una propiedad activa con la misma direccion y ciudad.");
        }

        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);

        if (esNueva) {
            guardarHistorialEstado(propiedadGuardada);
        }

        return propiedadGuardada;
    }

    private void validarPropiedad(Propiedad propiedad) {

        if (propiedad.getDireccion() == null || propiedad.getDireccion().trim().isEmpty()) {
            throw new RuntimeException("La direccion es obligatoria.");
        }

        if (propiedad.getCiudad() == null || propiedad.getCiudad().trim().isEmpty()) {
            throw new RuntimeException("La ciudad es obligatoria.");
        }

        if (propiedad.getTipoPropiedad() == null) {
            throw new RuntimeException("El tipo de propiedad es obligatorio.");
        }

        if (propiedad.getCantidadAmbientes() == null || propiedad.getCantidadAmbientes() <= 0) {
            throw new RuntimeException("La cantidad de ambientes debe ser un numero entero positivo.");
        }

        if (propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0) {
            throw new RuntimeException("Los metros cuadrados deben ser un numero positivo.");
        }

        if (propiedad.getDescripcion() == null || propiedad.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripcion es obligatoria.");
        }

        if (propiedad.getPropietario() == null || propiedad.getPropietario().getId() == null) {
            throw new RuntimeException("El propietario es obligatorio.");
        }
    }

    private Persona buscarYValidarPropietario(Propiedad propiedad) {

        Persona propietario = personaRepository.findById(propiedad.getPropietario().getId())
            .orElseThrow(() -> new RuntimeException("El propietario indicado no existe."));

        if (Boolean.TRUE.equals(propietario.getEliminada())) {
            throw new RuntimeException("El propietario indicado esta eliminado.");
        }

        return propietario;
    }

    private void guardarHistorialEstado(Propiedad propiedad) {

        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstadoPropiedad(propiedad.getEstadoPropiedad());
        historial.setFechaCambio(LocalDateTime.now());

        historialEstadoPropiedadRepository.save(historial);
    }
}