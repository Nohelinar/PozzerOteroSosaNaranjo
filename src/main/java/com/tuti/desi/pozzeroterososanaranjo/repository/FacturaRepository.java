package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Factura;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoFactura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

	List<Factura> findByEliminadaFalse();

	List<Factura> findByContratoIdAndEliminadaFalse(Long contratoId);

	List<Factura> findByContratoPropiedadIdAndEliminadaFalse(Long propiedadId);

	List<Factura> findByContratoInquilinoIdAndEliminadaFalse(Long inquilinoId);

	List<Factura> findByEstadoAndEliminadaFalse(EstadoFactura estado);

	List<Factura> findByFechaVencimientoBetweenAndEliminadaFalse(LocalDate desde, LocalDate hasta);
}
