package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Visita;

public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findAllByOrderByFechaHoraDesc();

    List<Visita> findByPublicacionIdOrderByFechaHoraDesc(Long publicacionId);

}
