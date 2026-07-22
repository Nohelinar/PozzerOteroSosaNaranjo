package com.tuti.desi.pozzeroterososanaranjo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Provincia;
import com.tuti.desi.pozzeroterososanaranjo.repository.ProvinciaRepository;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Provincia> listar() {
        return provinciaRepository.findAllByOrderByNombreAsc();
    }

    public Provincia buscarPorId(Long id) {
        return provinciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La provincia indicada no existe."));
    }

    @Transactional
    public Provincia guardar(Provincia provincia) {

        if (provincia.getNombre() == null || provincia.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la provincia es obligatorio.");
        }

        if (provinciaRepository.existsByNombreIgnoreCase(provincia.getNombre().trim())) {
            throw new RuntimeException("Ya existe una provincia con ese nombre.");
        }

        return provinciaRepository.save(provincia);
    }
}
