package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findAllByOrderByNombreAsc();

    List<Ciudad> findByProvinciaId(Long provinciaId);

    boolean existsByNombreIgnoreCaseAndProvinciaId(String nombre, Long provinciaId);

    boolean existsByNombreIgnoreCaseAndProvinciaIdAndIdNot(String nombre, Long provinciaId, Long id);

    boolean existsByProvinciaId(Long provinciaId);

}
