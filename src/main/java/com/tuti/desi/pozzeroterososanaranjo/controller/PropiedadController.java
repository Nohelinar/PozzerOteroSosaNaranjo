
package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.TipoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.service.PropiedadService;

@RestController
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping("/propiedades")
    public List<Propiedad> listarPropiedades(
        @RequestParam(required = false) String direccion,
        @RequestParam(required = false) String ciudad,
        @RequestParam(required = false) TipoPropiedad tipoPropiedad,
        @RequestParam(required = false) EstadoPropiedad estadoPropiedad
    ) {
        return propiedadService.listarConFiltros(direccion, ciudad, tipoPropiedad, estadoPropiedad);
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

    @PutMapping("/propiedades/{id}")
    public ResponseEntity<?> modificarPropiedad(@PathVariable Long id, @RequestBody Propiedad propiedad) {
        try {
            Propiedad propiedadModificada = propiedadService.modificar(id, propiedad);
            return ResponseEntity.ok(propiedadModificada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/propiedades/{id}")
    public ResponseEntity<?> eliminarPropiedad(@PathVariable Long id) {
        try {
            propiedadService.eliminarLogicamente(id);
            return ResponseEntity.ok("Propiedad eliminada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}