
package com.tuti.desi.pozzeroterososanaranjo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.repository.PropiedadRepository;

@Service
public class PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    public List<Propiedad> listarTodas() {
        return propiedadRepository.findAll();
    }

    public Propiedad guardar(Propiedad propiedad) {

        if (propiedad.getEstadoPropiedad() == null) {
            propiedad.setEstadoPropiedad(EstadoPropiedad.DISPONIBLE);
        }

        if (propiedad.getEliminada() == null) {
            propiedad.setEliminada(false);
        }

        if (propiedad.getId() == null &&
            propiedadRepository.existsByDireccionAndCiudadAndEliminadaFalse(
                propiedad.getDireccion(),
                propiedad.getCiudad()
            )) {
            throw new RuntimeException("Ya existe una propiedad activa con la misma direccion y ciudad.");
        }

        return propiedadRepository.save(propiedad);
    }
}