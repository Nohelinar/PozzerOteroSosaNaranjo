
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

        validarPropiedad(propiedad);

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
            throw new RuntimeException("La cantidad de ambientes debe ser un numero entero y positivo.");
        }

        if (propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0) {
            throw new RuntimeException("Los metros cuadrados deben ser un numero positivo");
        }

        if (propiedad.getDescripcion() == null || propiedad.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripcion es obligatoria.");
        }
    }
}