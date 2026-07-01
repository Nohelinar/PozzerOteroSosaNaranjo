package tuti.desi.controller.contratos;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tuti.desi.entities.Contrato;
import tuti.desi.entities.Persona;
import tuti.desi.entities.Propiedad;
import tuti.desi.entities.enums.EstadoContrato;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.services.ContratoService;
import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Controller
@RequestMapping("/contratosEditar")
public class ContratoEditarController {

    private final ContratoService servicio;
    private final PropiedadService propiedadService;
    private final PersonaService personaService;

    public ContratoEditarController(ContratoService servicio, PropiedadService propiedadService, PersonaService personaService) {
        this.servicio = servicio;
        this.propiedadService = propiedadService;
        this.personaService = personaService;
    }

    @GetMapping({"/", "", "/{id}"})
    public String preparaForm(Model modelo, @PathVariable Optional<Long> id) throws Excepcion {
        Contrato formBean = new Contrato();

        if (id.isPresent()) {
            formBean = servicio.getById(id.get());
        }

        modelo.addAttribute("formBean", formBean);
        modelo.addAttribute("propiedades", propiedadService.getAllActivas());
        modelo.addAttribute("personas", personaService.getAllActivas());
        modelo.addAttribute("estados", EstadoContrato.values());

        return "contratosEditar";
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws Excepcion {
        servicio.deleteById(id);
        return "redirect:/contratosBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute @Valid Contrato formBean,
            BindingResult result,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {

        if (action.equals("actionAceptar")) {
            if (result.hasErrors()) {
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("propiedades", propiedadService.getAllActivas());
                modelo.addAttribute("personas", personaService.getAllActivas());
                modelo.addAttribute("estados", EstadoContrato.values());
                return "contratosEditar";
            }

            try {
                // Cargar relaciones
                Propiedad propiedad = propiedadService.getById(formBean.getPropiedad().getId());
                Persona propietario = personaService.getById(formBean.getPropietario().getId());
                Persona inquilino = personaService.getById(formBean.getInquilino().getId());

                formBean.setPropiedad(propiedad);
                formBean.setPropietario(propietario);
                formBean.setInquilino(inquilino);

                // Si es nuevo, guardar
                if (formBean.getId() == null) {
                    formBean.setEstadoContrato(EstadoContrato.Borrador);
                }

                Contrato saved = servicio.save(formBean);

                // Registrar estado inicial si es nuevo
                if (formBean.getId() == null) {
                    servicio.cambiarEstado(saved, saved.getEstadoContrato());
                }

                return "redirect:/contratosBuscar";

            } catch (Excepcion e) {
                if (e.getAtributo() == null) {
                    ObjectError error = new ObjectError("globalError", e.getMessage());
                    result.addError(error);
                } else {
                    FieldError error = new FieldError("formBean", e.getAtributo(), e.getMessage());
                    result.addError(error);
                }
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("propiedades", propiedadService.getAllActivas());
                modelo.addAttribute("personas", personaService.getAllActivas());
                modelo.addAttribute("estados", EstadoContrato.values());
                return "contratosEditar";
            }

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/contratosBuscar";
        }

        return "redirect:/";
    }
}