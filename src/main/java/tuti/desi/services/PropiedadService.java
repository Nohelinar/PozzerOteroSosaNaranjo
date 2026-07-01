package tuti.desi.services;

import java.util.List;


import tuti.desi.entities.Propiedad;

import tuti.desi.exceptions.Excepcion;


import tuti.desi.entities.enums.EstadoDisponibilidad;
import tuti.desi.entities.enums.TipoPropiedad;





public interface PropiedadService {
    Propiedad save(Propiedad propiedad) throws Excepcion;
    Propiedad getById(Long id) throws Excepcion;
    List<Propiedad> getAll();
    List<Propiedad> getAllActivas();
    void deleteById(Long id) throws Excepcion;
    List<Propiedad> filter(String direccion, String ciudad, TipoPropiedad tipo, EstadoDisponibilidad estado);
    boolean existsByDireccionAndCiudad(String direccion, String ciudad, Long id);
    void cambiarEstado(Propiedad propiedad, EstadoDisponibilidad nuevoEstado);
}