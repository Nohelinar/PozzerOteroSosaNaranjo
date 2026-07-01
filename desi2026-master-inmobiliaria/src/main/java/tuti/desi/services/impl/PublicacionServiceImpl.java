package tuti.desi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entities.Propiedad;
import tuti.desi.entities.Publicacion;
import tuti.desi.entities.enums.EstadoPublicacion;
import tuti.desi.exceptions.EntidadNoEncontradaException;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.repository.PublicacionRepository;
import tuti.desi.services.PropiedadService;
import tuti.desi.services.PublicacionService;

@Service
@Transactional
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository repo;
    private final PropiedadService propiedadService;

    public PublicacionServiceImpl(PublicacionRepository repo, PropiedadService propiedadService) {
        this.repo = repo;
        this.propiedadService = propiedadService;
    }

    @Override
    @Transactional
    public Publicacion save(Publicacion publicacion) throws Excepcion {
        // Validar que la propiedad exista y esté disponible
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new Excepcion("La propiedad es obligatoria", "propiedad");
        }

        Propiedad propiedad = propiedadService.getById(publicacion.getPropiedad().getId());
        
        // Solo se puede publicar una propiedad disponible
        if (publicacion.getId() == null) {
            // Nueva publicación: validar que la propiedad esté disponible
            if (!propiedad.getEstadoDisponibilidad().name().equals("Disponible")) {
                throw new Excepcion("La propiedad debe estar disponible para publicar", "propiedad");
            }
        }

        // Validar que no exista otra publicación activa para la misma propiedad
        if (publicacion.getEstadoPublicacion() == EstadoPublicacion.Activa) {
            if (repo.existsPublicacionActivaByPropiedad(propiedad.getId(), publicacion.getId())) {
                throw new Excepcion("Ya existe una publicación activa para esta propiedad", "propiedad");
            }
        }

        // Si es nueva, establecer fecha de publicación
        if (publicacion.getId() == null) {
            publicacion.setFechaPublicacion(java.time.LocalDate.now());
            publicacion.setEstadoPublicacion(EstadoPublicacion.Activa);
        }

        return repo.save(publicacion);
    }

    @Override
    public Publicacion getById(Long id) throws Excepcion {
        return repo.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Publicacion", id));
    }

    @Override
    public List<Publicacion> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Publicacion> getAllActivas() {
        return repo.findAllActivas();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Publicacion publicacion = getById(id);
        
        // Solo se pueden eliminar publicaciones activas
        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.Activa) {
            throw new Excepcion("Solo se pueden eliminar publicaciones activas");
        }
        
        publicacion.setEliminado(true);
        repo.save(publicacion);
    }

    @Override
    public List<Publicacion> filter(Long propiedadId, String ciudad, EstadoPublicacion estado, Double precioMin, Double precioMax) {
        return repo.filter(propiedadId, ciudad, estado, precioMin, precioMax);
    }

    @Override
    public boolean existsPublicacionActivaByPropiedad(Long propiedadId, Long id) {
        return repo.existsPublicacionActivaByPropiedad(propiedadId, id);
    }

    @Override
    @Transactional
    public void cambiarEstado(Publicacion publicacion, EstadoPublicacion nuevoEstado) {
        EstadoPublicacion estadoAnterior = publicacion.getEstadoPublicacion();
        
        if (estadoAnterior != nuevoEstado) {
            publicacion.setEstadoPublicacion(nuevoEstado);
            publicacion.agregarCambioEstado(estadoAnterior, nuevoEstado);
            repo.save(publicacion);
        }
    }
}