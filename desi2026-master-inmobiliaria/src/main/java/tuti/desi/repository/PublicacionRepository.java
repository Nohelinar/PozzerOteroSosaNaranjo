package tuti.desi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entities.Publicacion;
import tuti.desi.entities.enums.EstadoPublicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE p.eliminado = false")
    List<Publicacion> findAllActivas();

    @Query("""
        SELECT p FROM Publicacion p
        WHERE p.eliminado = false
          AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
          AND (:ciudad IS NULL OR LOWER(p.propiedad.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
          AND (:estado IS NULL OR p.estadoPublicacion = :estado)
          AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
          AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
    """)
    List<Publicacion> filter(
        @Param("propiedadId") Long propiedadId,
        @Param("ciudad") String ciudad,
        @Param("estado") EstadoPublicacion estado,
        @Param("precioMin") Double precioMin,
        @Param("precioMax") Double precioMax
    );

    @Query("""
        SELECT COUNT(p) > 0 FROM Publicacion p
        WHERE p.eliminado = false
          AND p.estadoPublicacion = 'Activa'
          AND p.propiedad.id = :propiedadId
          AND (:id IS NULL OR p.id <> :id)
    """)
    boolean existsPublicacionActivaByPropiedad(@Param("propiedadId") Long propiedadId, @Param("id") Long id);

    @Query("""
        SELECT COUNT(p) > 0 FROM Publicacion p
        WHERE p.eliminado = false
          AND p.propiedad.id = :propiedadId
          AND p.estadoPublicacion = 'Activa'
    """)
    boolean hasPublicacionActiva(@Param("propiedadId") Long propiedadId);
}