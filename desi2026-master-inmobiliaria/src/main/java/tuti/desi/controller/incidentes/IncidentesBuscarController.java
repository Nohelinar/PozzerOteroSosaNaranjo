package tuti.desi.controller.incidentes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/incidentesBuscar")
public class IncidentesBuscarController {

    @GetMapping
    public String mostrarEnConstruccion(Model modelo) {
        modelo.addAttribute("titulo", "Incidentes - En Construcción");
        modelo.addAttribute("mensaje", "El módulo de Incidentes está en desarrollo. Próximamente disponible.");
        return "incidentesBuscar";
    }
}