package com.tuti.desi.pozzeroterososanaranjo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Ciudad;
import com.tuti.desi.pozzeroterososanaranjo.entity.HistorialEstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoContrato;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.TipoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.repository.ContratoRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.CiudadRepository;
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
    private CiudadRepository ciudadRepository;

    @Autowired
    private HistorialEstadoPropiedadRepository historialEstadoPropiedadRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Propiedad> listarTodas() {
        return propiedadRepository.findByEliminadaFalse();
    }

    public List<Propiedad> listarConFiltros(
            String direccion,
            String ciudad,
            TipoPropiedad tipoPropiedad,
            EstadoPropiedad estadoPropiedad
    ) {
        List<Propiedad> propiedades = propiedadRepository.findByEliminadaFalse();
        List<Propiedad> propiedadesFiltradas = new ArrayList<>();

        for (Propiedad propiedad : propiedades) {

            boolean cumpleDireccion = direccion == null || direccion.trim().isEmpty()
                    || contieneTexto(propiedad.getDireccion(), direccion);

            boolean cumpleCiudad = ciudad == null || ciudad.trim().isEmpty()
                    || (propiedad.getCiudad() != null && contieneTexto(propiedad.getCiudad().getNombre(), ciudad));

            boolean cumpleTipo = tipoPropiedad == null
                    || tipoPropiedad.equals(propiedad.getTipoPropiedad());

            boolean cumpleEstado = estadoPropiedad == null
                    || estadoPropiedad.equals(propiedad.getEstadoPropiedad());

            if (cumpleDireccion && cumpleCiudad && cumpleTipo && cumpleEstado) {
                propiedadesFiltradas.add(propiedad);
            }
        }

        return propiedadesFiltradas;
    }

    public Propiedad buscarPorId(Long id) {

        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La propiedad indicada no existe."));

        if (Boolean.TRUE.equals(propiedad.getEliminada())) {
            throw new RuntimeException("La propiedad indicada esta eliminada.");
        }

        return propiedad;
    }
    @Transactional
    public Propiedad guardar(Propiedad propiedad) {

        boolean esNueva = propiedad.getId() == null;

        validarPropiedad(propiedad);

        Persona propietario = buscarYValidarPropietario(propiedad);
        propiedad.setPropietario(propietario);

        propiedad.setCiudad(buscarYValidarCiudad(propiedad));

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

    @Transactional
    public Propiedad modificar(Long id, Propiedad propiedadActualizada) {

        Propiedad propiedadExistente = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La propiedad indicada no existe."));

        if (Boolean.TRUE.equals(propiedadExistente.getEliminada())) {
            throw new RuntimeException("No se puede modificar una propiedad eliminada.");
        }

        validarPropiedad(propiedadActualizada);

        if (propiedadActualizada.getEstadoPropiedad() == null) {
            throw new RuntimeException("El estado de propiedad es obligatorio.");
        }

        Persona propietario = buscarYValidarPropietario(propiedadActualizada);
        Ciudad ciudad = buscarYValidarCiudad(propiedadActualizada);

        if (propiedadRepository.existsByDireccionAndCiudadAndEliminadaFalseAndIdNot(
                propiedadActualizada.getDireccion(),
                propiedadActualizada.getCiudad(),
                id
        )) {
            throw new RuntimeException("Ya existe otra propiedad activa con la misma direccion y ciudad.");
        }

        if ((propiedadActualizada.getEstadoPropiedad() == EstadoPropiedad.DISPONIBLE
                || propiedadActualizada.getEstadoPropiedad() == EstadoPropiedad.INACTIVA)
                && contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalse(id, EstadoContrato.ACTIVO)) {
            throw new RuntimeException(
                    "No se puede cambiar el estado a disponible o inactiva porque la propiedad tiene un contrato activo. Finalice o rescinda el contrato primero.");
        }

        EstadoPropiedad estadoAnterior = propiedadExistente.getEstadoPropiedad();

        propiedadExistente.setDireccion(propiedadActualizada.getDireccion());
        propiedadExistente.setCiudad(ciudad);
        propiedadExistente.setTipoPropiedad(propiedadActualizada.getTipoPropiedad());
        propiedadExistente.setCantidadAmbientes(propiedadActualizada.getCantidadAmbientes());
        propiedadExistente.setMetrosCuadrados(propiedadActualizada.getMetrosCuadrados());
        propiedadExistente.setDescripcion(propiedadActualizada.getDescripcion());
        propiedadExistente.setEstadoPropiedad(propiedadActualizada.getEstadoPropiedad());
        propiedadExistente.setPropietario(propietario);

        Propiedad propiedadGuardada = propiedadRepository.save(propiedadExistente);

        if (estadoAnterior != propiedadGuardada.getEstadoPropiedad()) {
            guardarHistorialEstado(propiedadGuardada);
        }

        return propiedadGuardada;
    }

    @Transactional
    public void eliminarLogicamente(Long id) {

        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La propiedad indicada no existe."));

        if (Boolean.TRUE.equals(propiedad.getEliminada())) {
            throw new RuntimeException("La propiedad ya se encuentra eliminada.");
        }

        if (contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalse(id, EstadoContrato.ACTIVO)) {
            throw new RuntimeException("No se puede eliminar la propiedad porque tiene un contrato activo vigente.");
        }

        propiedad.setEliminada(true);
        propiedadRepository.save(propiedad);
    }

    private void validarPropiedad(Propiedad propiedad) {

        if (propiedad.getDireccion() == null || propiedad.getDireccion().trim().isEmpty()) {
            throw new RuntimeException("La direccion es obligatoria.");
        }

        if (propiedad.getCiudad() == null || propiedad.getCiudad().getId() == null) {
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

    private Ciudad buscarYValidarCiudad(Propiedad propiedad) {

        return ciudadRepository.findById(propiedad.getCiudad().getId())
                .orElseThrow(() -> new RuntimeException("La ciudad indicada no existe."));
    }

    private void guardarHistorialEstado(Propiedad propiedad) {

        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstadoPropiedad(propiedad.getEstadoPropiedad());
        historial.setFechaCambio(LocalDateTime.now());

        historialEstadoPropiedadRepository.save(historial);
    }

    private boolean contieneTexto(String textoCompleto, String textoBuscado) {

        if (textoCompleto == null) {
            return false;
        }

        return textoCompleto.toLowerCase().contains(textoBuscado.toLowerCase().trim());
    }
}