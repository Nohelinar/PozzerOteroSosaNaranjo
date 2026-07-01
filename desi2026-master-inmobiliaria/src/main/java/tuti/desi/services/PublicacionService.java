package tuti.desi.services;

import java.util.List;

import tuti.desi.entities.Publicacion;
import tuti.desi.entities.enums.EstadoPublicacion;
import tuti.desi.exceptions.Excepcion;

public interface PublicacionService {
    Publicacion save(Publicacion publicacion) throws Excepcion;
    Publicacion getById(Long id) throws Excepcion;
    List<Publicacion> getAll();
    List<Publicacion> getAllActivas();
    void deleteById(Long id) throws Excepcion;
    List<Publicacion> filter(Long propiedadId, String ciudad, EstadoPublicacion estado, Double precioMin, Double precioMax);
    boolean existsPublicacionActivaByPropiedad(Long propiedadId, Long id);
    void cambiarEstado(Publicacion publicacion, EstadoPublicacion nuevoEstado);
}