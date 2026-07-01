package tuti.desi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entities.Factura;
import tuti.desi.entities.enums.EstadoFactura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f WHERE f.eliminado = false")
    List<Factura> findAllActivas();

    @Query("""
        SELECT f FROM Factura f
        WHERE f.eliminado = false
          AND (:contratoId IS NULL OR f.contrato.id = :contratoId)
          AND (:propiedadId IS NULL OR f.contrato.propiedad.id = :propiedadId)
          AND (:inquilinoId IS NULL OR f.contrato.inquilino.id = :inquilinoId)
          AND (:estado IS NULL OR f.estadoFactura = :estado)
          AND (:fechaVencimientoDesde IS NULL OR f.fechaVencimiento >= :fechaVencimientoDesde)
          AND (:fechaVencimientoHasta IS NULL OR f.fechaVencimiento <= :fechaVencimientoHasta)
    """)
    List<Factura> filter(
        @Param("contratoId") Long contratoId,
        @Param("propiedadId") Long propiedadId,
        @Param("inquilinoId") Long inquilinoId,
        @Param("estado") EstadoFactura estado,
        @Param("fechaVencimientoDesde") LocalDate fechaVencimientoDesde,
        @Param("fechaVencimientoHasta") LocalDate fechaVencimientoHasta
    );

    @Query("""
        SELECT COUNT(f) > 0 FROM Factura f
        WHERE f.eliminado = false
          AND f.estadoFactura = 'Pagada'
          AND f.contrato.id = :contratoId
          AND f.fechaEmision = :fechaEmision
    """)
    boolean existsFacturaPagadaByContratoAndFecha(@Param("contratoId") Long contratoId, @Param("fechaEmision") LocalDate fechaEmision);
}