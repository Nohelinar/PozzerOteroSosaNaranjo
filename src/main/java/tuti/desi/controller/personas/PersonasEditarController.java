package tuti.desi.controller.personas;

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
import tuti.desi.exceptions.Excepcion;
import tuti.desi.services.PersonaService;

@Controller
@RequestMapping("/personasEditar")
public class PersonasEditarController {

    private final PersonaService servicio;

    public PersonasEditarController(PersonaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping({"/", "", "/{id}"})
    public String preparaForm(Model modelo, @PathVariable Optional<Long> id) throws Excepcion {  // ← AGREGAR
        if (id.isPresent()) {
            Persona entity = servicio.getById(id.get());
            modelo.addAttribute("formBean", entity);
        } else {
            modelo.addAttribute("formBean", new Persona());
        }
        return "personasEditar";
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws Excepcion {  // ← AGREGAR
        servicio.deleteById(id);
        return "redirect:/personasBuscar";
    }

    @PostMapping
    public String submit(@ModelAttribute Persona formBean, ModelMap modelo, @RequestParam String action) throws Excepcion {  // ← AGREGAR
        if (action.equals("actionAceptar")) {
            servicio.save(formBean);
            return "redirect:/personasBuscar";
        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/personasBuscar";
        }
        return "redirect:/";
    }
}