package tuti.desi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entities.Contrato;
import tuti.desi.entities.enums.EstadoContrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("SELECT c FROM Contrato c WHERE c.eliminado = false")
    List<Contrato> findAllActivos();

    @Query("""
        SELECT c FROM Contrato c
        WHERE c.eliminado = false
          AND (:propiedadId IS NULL OR c.propiedad.id = :propiedadId)
          AND (:inquilinoId IS NULL OR c.inquilino.id = :inquilinoId)
          AND (:estado IS NULL OR c.estadoContrato = :estado)
          AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
    """)
    List<Contrato> filter(
        @Param("propiedadId") Long propiedadId,
        @Param("inquilinoId") Long inquilinoId,
        @Param("estado") EstadoContrato estado,
        @Param("fechaInicio") LocalDate fechaInicio
    );

    @Query("""
        SELECT COUNT(c) > 0 FROM Contrato c
        WHERE c.eliminado = false
          AND c.estadoContrato = 'Activo'
          AND c.propiedad.id = :propiedadId
          AND (:id IS NULL OR c.id <> :id)
    """)
    boolean existsContratoActivoByPropiedad(@Param("propiedadId") Long propiedadId, @Param("id") Long id);

    @Query("""
        SELECT COUNT(c) > 0 FROM Contrato c
        WHERE c.eliminado = false
          AND c.estadoContrato = 'Activo'
          AND c.propiedad.id = :propiedadId
    """)
    boolean hasContratoActivo(@Param("propiedadId") Long propiedadId);
}