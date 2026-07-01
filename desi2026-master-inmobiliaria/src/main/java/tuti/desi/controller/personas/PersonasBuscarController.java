package tuti.desi.controller.personas;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entities.Persona;
import tuti.desi.services.PersonaService;

@Controller
@RequestMapping("/personasBuscar")
public class PersonasBuscarController {

    private final PersonaService servicio;

    public PersonasBuscarController(PersonaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new PersonasBuscarForm());
        return "personasBuscar";
    }

    @PostMapping
    public String submit(@ModelAttribute PersonasBuscarForm formBean, ModelMap modelo, @RequestParam String action) {
        if (action.equals("actionBuscar")) {
            List<Persona> resultados = servicio.filter(
                formBean.getNombre(),
                formBean.getApellido(),
                formBean.getEmail()
            );
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("formBean", formBean);
            return "personasBuscar";
        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        } else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/personasEditar";
        }
        return "redirect:/";
    }
}