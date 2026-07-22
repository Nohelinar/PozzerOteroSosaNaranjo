package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Incidente;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    List<Incidente> findByEliminadoFalseOrderByFechaAltaDesc();

    List<Incidente> findByPropiedadIdAndEliminadoFalse(Long propiedadId);

}
