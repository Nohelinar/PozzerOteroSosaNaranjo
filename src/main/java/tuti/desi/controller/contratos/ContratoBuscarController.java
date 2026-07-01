package tuti.desi.controller.contratos;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entities.Contrato;
import tuti.desi.entities.enums.EstadoContrato;
import tuti.desi.services.ContratoService;
import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Controller
@RequestMapping("/contratosBuscar")
public class ContratoBuscarController {

    private final ContratoService servicio;
    private final PropiedadService propiedadService;
    private final PersonaService personaService;

    public ContratoBuscarController(ContratoService servicio, PropiedadService propiedadService, PersonaService personaService) {
        this.servicio = servicio;
        this.propiedadService = propiedadService;
        this.personaService = personaService;
    }

    @GetMapping
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new ContratoBuscarForm());
        modelo.addAttribute("propiedades", propiedadService.getAllActivas());
        modelo.addAttribute("personas", personaService.getAllActivas());
        modelo.addAttribute("estados", EstadoContrato.values());
        return "contratosBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute ContratoBuscarForm formBean,
            ModelMap modelo,
            @RequestParam String action) {

        if (action.equals("actionBuscar")) {
            List<Contrato> resultados = servicio.filter(
                formBean.getPropiedadId(),
                formBean.getInquilinoId(),
                formBean.getEstado(),
                formBean.getFechaInicio()
            );
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("formBean", formBean);
            modelo.addAttribute("propiedades", propiedadService.getAllActivas());
            modelo.addAttribute("personas", personaService.getAllActivas());
            modelo.addAttribute("estados", EstadoContrato.values());
            return "contratosBuscar";

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/contratosBuscar";

        } else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/contratosEditar";
        }

        return "redirect:/";
    }
}