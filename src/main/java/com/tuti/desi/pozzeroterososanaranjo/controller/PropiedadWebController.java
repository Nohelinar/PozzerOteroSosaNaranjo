
package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.entity.Propiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.enums.TipoPropiedad;
import com.tuti.desi.pozzeroterososanaranjo.service.CiudadService;
import com.tuti.desi.pozzeroterososanaranjo.service.PersonaService;
import com.tuti.desi.pozzeroterososanaranjo.service.PropiedadService;

@Controller
public class PropiedadWebController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping("/propiedades/buscar")
    public String buscarPropiedades(
        @RequestParam(required = false) String direccion,
        @RequestParam(required = false) String ciudad,
        @RequestParam(required = false) TipoPropiedad tipoPropiedad,
        @RequestParam(required = false) EstadoPropiedad estadoPropiedad,
        Model model
    ) {
        model.addAttribute("propiedades", propiedadService.listarConFiltros(direccion, ciudad, tipoPropiedad, estadoPropiedad));
        model.addAttribute("direccion", direccion);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("tipoPropiedadSeleccionada", tipoPropiedad);
        model.addAttribute("estadoPropiedadSeleccionado", estadoPropiedad);
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosPropiedad", EstadoPropiedad.values());

        return "propiedadesBuscar";
    }

    @GetMapping("/propiedades/registrar")
    public String registrarPropiedad(Model model) {
        Propiedad propiedad = new Propiedad();
        propiedad.setEstadoPropiedad(EstadoPropiedad.DISPONIBLE);
        propiedad.setPropietario(new Persona());

        cargarDatosFormulario(model, propiedad);

        return "propiedadesRegistrar";
    }

    @PostMapping("/propiedades/registrar")
    public String guardarNuevaPropiedad(Propiedad propiedad, Model model) {
        try {
            propiedadService.guardar(propiedad);
            return "redirect:/propiedades/buscar";
        } catch (RuntimeException e) {
            if (propiedad.getPropietario() == null) {
                propiedad.setPropietario(new Persona());
            }

            cargarDatosFormulario(model, propiedad);
            model.addAttribute("error", e.getMessage());

            return "propiedadesRegistrar";
        }
    }

    @GetMapping("/propiedades/editar/{id}")
    public String editarPropiedad(@PathVariable Long id, Model model) {
        try {
            Propiedad propiedad = propiedadService.buscarPorId(id);

            if (propiedad.getPropietario() == null) {
                propiedad.setPropietario(new Persona());
            }

            cargarDatosFormulario(model, propiedad);

            return "propiedadesEditar";

        } catch (RuntimeException e) {
            model.addAttribute("propiedades", propiedadService.listarConFiltros(null, null, null, null));
            model.addAttribute("tiposPropiedad", TipoPropiedad.values());
            model.addAttribute("estadosPropiedad", EstadoPropiedad.values());
            model.addAttribute("error", e.getMessage());

            return "propiedadesBuscar";
        }
    }

    @PostMapping("/propiedades/guardar/{id}")
    public String guardarModificacion(@PathVariable Long id, Propiedad propiedad, Model model) {
        try {
            propiedadService.modificar(id, propiedad);
            return "redirect:/propiedades/buscar";
        } catch (RuntimeException e) {
            if (propiedad.getPropietario() == null) {
                propiedad.setPropietario(new Persona());
            }

            cargarDatosFormulario(model, propiedad);
            model.addAttribute("error", e.getMessage());

            return "propiedadesEditar";
        }
    }

    @PostMapping("/propiedades/eliminar/{id}")
    public String eliminarPropiedad(@PathVariable Long id, Model model) {
        try {
            propiedadService.eliminarLogicamente(id);
            return "redirect:/propiedades/buscar";
        } catch (RuntimeException e) {
            model.addAttribute("propiedades", propiedadService.listarConFiltros(null, null, null, null));
            model.addAttribute("tiposPropiedad", TipoPropiedad.values());
            model.addAttribute("estadosPropiedad", EstadoPropiedad.values());
            model.addAttribute("error", e.getMessage());

            return "propiedadesBuscar";
        }
    }

    private void cargarDatosFormulario(Model model, Propiedad propiedad) {
        List<Persona> personas = personaService.listarNoEliminadas();

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("personas", personas);
        model.addAttribute("ciudades", ciudadService.listar());
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosPropiedad", EstadoPropiedad.values());
    }
}