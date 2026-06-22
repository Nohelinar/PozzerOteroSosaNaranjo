
package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.service.PropiedadService;

@RestController
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping("/propiedades")
    public List<Propiedad> listarPropiedades() {
        return propiedadService.listarTodas();
    }

    @PostMapping("/propiedades")
    public ResponseEntity<?> crearPropiedad(@RequestBody Propiedad propiedad) {
        try {
            Propiedad propiedadGuardada = propiedadService.guardar(propiedad);
            return ResponseEntity.ok(propiedadGuardada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/propiedades/{id}")
    public ResponseEntity<?> eliminarPropiedad(@PathVariable Long id) {
        try {
            propiedadService.eliminarLogicamente(id);
            return ResponseEntity.ok("Propiedad eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}