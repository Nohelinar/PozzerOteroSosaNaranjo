package tuti.desi.controller.propiedad;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tuti.desi.entities.Propiedad;
import tuti.desi.services.PropiedadService;
import tuti.desi.entities.enums.TipoPropiedad;
import tuti.desi.entities.enums.EstadoDisponibilidad;

@Controller
@RequestMapping("/propiedadesBuscar")
public class PropiedadBuscarController {

    private final PropiedadService servicio;

    public PropiedadBuscarController(PropiedadService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new PropiedadBuscarForm());
        modelo.addAttribute("tipos", TipoPropiedad.values());
        modelo.addAttribute("estados", EstadoDisponibilidad.values());
        return "propiedadesBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute PropiedadBuscarForm formBean,
            ModelMap modelo,
            @RequestParam String action) {

        if (action.equals("actionBuscar")) {
            List<Propiedad> resultados = servicio.filter(
                formBean.getDireccion(),
                formBean.getCiudad(),
                formBean.getTipo(),
                formBean.getEstado()
            );
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("formBean", formBean);
            modelo.addAttribute("tipos", TipoPropiedad.values());
            modelo.addAttribute("estados", EstadoDisponibilidad.values());
            return "propiedadesBuscar";

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/propiedadesBuscar";

        } else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/propiedadesEditar";
        }

        return "redirect:/";
    }
}