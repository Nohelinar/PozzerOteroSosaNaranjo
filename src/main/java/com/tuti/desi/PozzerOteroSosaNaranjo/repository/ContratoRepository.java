package com.tuti.desi.PozzerOteroSosaNaranjo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Contrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoContrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	List<Contrato> findByEliminadoFalse();

	List<Contrato> findByPropiedadIdAndEstado(Long propiedadId, EstadoContrato estado);

	List<Contrato> findByPropiedadIdAndEliminadoFalse(Long propiedadId);

	List<Contrato> findByInquilinoIdAndEliminadoFalse(Long inquilinoId);

	List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

	List<Contrato> findByFechaInicioBetweenAndEliminadoFalse(LocalDate fechaDesde, LocalDate fechaHasta);
}
