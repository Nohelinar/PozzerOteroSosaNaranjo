package tuti.desi.repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import tuti.desi.entities.Propiedad;


import tuti.desi.entities.enums.TipoPropiedad;
import tuti.desi.entities.enums.EstadoDisponibilidad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    @Query("SELECT p FROM Propiedad p WHERE p.eliminado = false")
    List<Propiedad> findAllActivas();

    @Query("""
        SELECT p FROM Propiedad p
        WHERE p.eliminado = false
          AND (:direccion IS NULL OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
          AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
          AND (:tipo IS NULL OR p.tipo = :tipo)
          AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)
    """)
    List<Propiedad> filter(
        @Param("direccion") String direccion,
        @Param("ciudad") String ciudad,
        @Param("tipo") TipoPropiedad tipo,
        @Param("estado") EstadoDisponibilidad estado
    );

    @Query("""
        SELECT COUNT(p) > 0 FROM Propiedad p
        WHERE p.eliminado = false
          AND LOWER(p.direccion) = LOWER(:direccion)
          AND LOWER(p.ciudad) = LOWER(:ciudad)
          AND (:id IS NULL OR p.id <> :id)
    """)
    boolean existsByDireccionAndCiudad(
        @Param("direccion") String direccion,
        @Param("ciudad") String ciudad,
        @Param("id") Long id
    );
}