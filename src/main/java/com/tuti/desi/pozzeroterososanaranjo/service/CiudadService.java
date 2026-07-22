package com.tuti.desi.pozzeroterososanaranjo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuti.desi.pozzeroterososanaranjo.entity.Ciudad;
import com.tuti.desi.pozzeroterososanaranjo.repository.CiudadRepository;
import com.tuti.desi.pozzeroterososanaranjo.repository.ProvinciaRepository;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Ciudad> listar() {
        return ciudadRepository.findAllByOrderByNombreAsc();
    }

    public Ciudad buscarPorId(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La ciudad indicada no existe."));
    }

    @Transactional
    public Ciudad guardar(Ciudad ciudad) {

        if (ciudad.getNombre() == null || ciudad.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la ciudad es obligatorio.");
        }

        if (ciudad.getProvincia() == null || ciudad.getProvincia().getId() == null) {
            throw new RuntimeException("La provincia es obligatoria.");
        }

        Long provinciaId = ciudad.getProvincia().getId();

        if (!provinciaRepository.existsById(provinciaId)) {
            throw new RuntimeException("La provincia indicada no existe.");
        }

        if (ciudadRepository.existsByNombreIgnoreCaseAndProvinciaId(ciudad.getNombre().trim(), provinciaId)) {
            throw new RuntimeException("Ya existe una ciudad con ese nombre en la provincia seleccionada.");
        }

        return ciudadRepository.save(ciudad);
    }
}
