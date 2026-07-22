package com.tuti.desi.pozzeroterososanaranjo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuti.desi.pozzeroterososanaranjo.entity.Provincia;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    List<Provincia> findAllByOrderByNombreAsc();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
