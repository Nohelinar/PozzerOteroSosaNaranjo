package tuti.desi.controller.propiedad;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entities.Persona;
import tuti.desi.entities.Propiedad;
import tuti.desi.entities.enums.EstadoDisponibilidad;
import tuti.desi.entities.enums.TipoPropiedad;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Controller
@RequestMapping("/propiedadesEditar")
public class PropiedadEditarController {

    private final PropiedadService servicio;
    private final PersonaService personaService;

    public PropiedadEditarController(PropiedadService servicio, PersonaService personaService) {
        this.servicio = servicio;
        this.personaService = personaService;
    }

    @GetMapping({"/", "", "/{id}"})
    public String preparaForm(Model modelo, @PathVariable Optional<Long> id) throws Excepcion {
        PropiedadForm formBean = new PropiedadForm();
        
        if (id.isPresent()) {
            Propiedad entity = servicio.getById(id.get());
            formBean.setId(entity.getId());
            formBean.setDireccion(entity.getDireccion());
            formBean.setCiudad(entity.getCiudad());
            formBean.setTipo(entity.getTipo().name());
            formBean.setCantidadAmbientes(entity.getCantidadAmbientes());
            formBean.setMetrosCuadrados(entity.getMetrosCuadrados());
            formBean.setDescripcion(entity.getDescripcion());
            formBean.setEstadoDisponibilidad(entity.getEstadoDisponibilidad().name());
            if (entity.getPropietario() != null) {
                formBean.setPropietarioId(entity.getPropietario().getId());
            }
        }
        
        modelo.addAttribute("formBean", formBean);
        modelo.addAttribute("tipos", TipoPropiedad.values());
        modelo.addAttribute("estados", EstadoDisponibilidad.values());
        modelo.addAttribute("personas", personaService.getAllActivas());
        
        return "propiedadesEditar";
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws Excepcion {
        servicio.deleteById(id);
        return "redirect:/propiedadesBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute PropiedadForm formBean,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {
        
        System.out.println("=== POST RECIBIDO ===");
        System.out.println("Action: " + action);
        System.out.println("ID: " + formBean.getId());
        System.out.println("Direccion: " + formBean.getDireccion());
        System.out.println("Ciudad: " + formBean.getCiudad());
        
        if (action.equals("actionAceptar")) {
            try {
                Propiedad propiedad = new Propiedad();
                if (formBean.getId() != null) {
                    propiedad = servicio.getById(formBean.getId());
                }
                
                propiedad.setDireccion(formBean.getDireccion());
                propiedad.setCiudad(formBean.getCiudad());
                propiedad.setTipo(TipoPropiedad.valueOf(formBean.getTipo()));
                propiedad.setCantidadAmbientes(formBean.getCantidadAmbientes());
                propiedad.setMetrosCuadrados(formBean.getMetrosCuadrados());
                propiedad.setDescripcion(formBean.getDescripcion());
                propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.valueOf(formBean.getEstadoDisponibilidad()));
                
                Persona propietario = personaService.getById(formBean.getPropietarioId());
                propiedad.setPropietario(propietario);
                
                Propiedad saved = servicio.save(propiedad);
                
                if (formBean.getId() == null) {
                    servicio.cambiarEstado(saved, saved.getEstadoDisponibilidad());
                }
                
                System.out.println("Propiedad guardada con ID: " + saved.getId());
                return "redirect:/propiedadesBuscar";
                
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
                modelo.addAttribute("error", e.getMessage());
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("tipos", TipoPropiedad.values());
                modelo.addAttribute("estados", EstadoDisponibilidad.values());
                modelo.addAttribute("personas", personaService.getAllActivas());
                return "propiedadesEditar";
            }
            
        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/propiedadesBuscar";
        }
        
        return "redirect:/";
    }
}