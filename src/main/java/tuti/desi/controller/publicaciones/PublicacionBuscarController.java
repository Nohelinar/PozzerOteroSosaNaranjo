package tuti.desi.controller.publicaciones;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entities.Publicacion;
import tuti.desi.entities.enums.EstadoPublicacion;
import tuti.desi.services.PropiedadService;
import tuti.desi.services.PublicacionService;

@Controller
@RequestMapping("/publicacionesBuscar")
public class PublicacionBuscarController {

    private final PublicacionService servicio;
    private final PropiedadService propiedadService;

    public PublicacionBuscarController(PublicacionService servicio, PropiedadService propiedadService) {
        this.servicio = servicio;
        this.propiedadService = propiedadService;
    }

    @GetMapping
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new PublicacionBuscarForm());
        modelo.addAttribute("propiedades", propiedadService.getAllActivas());
        modelo.addAttribute("estados", EstadoPublicacion.values());
        return "publicacionesBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute PublicacionBuscarForm formBean,
            ModelMap modelo,
            @RequestParam String action) {

        if (action.equals("actionBuscar")) {
            List<Publicacion> resultados = servicio.filter(
                formBean.getPropiedadId(),
                formBean.getCiudad(),
                formBean.getEstado(),
                formBean.getPrecioMin(),
                formBean.getPrecioMax()
            );
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("formBean", formBean);
            modelo.addAttribute("propiedades", propiedadService.getAllActivas());
            modelo.addAttribute("estados", EstadoPublicacion.values());
            return "publicacionesBuscar";

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/publicacionesBuscar";

        } else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/publicacionesEditar";
        }

        return "redirect:/";
    }
}