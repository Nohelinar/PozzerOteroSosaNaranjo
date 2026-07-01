package tuti.desi.controller.publicaciones;

import java.time.LocalDate;
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

import tuti.desi.entities.Publicacion;
import tuti.desi.entities.Propiedad;
import tuti.desi.entities.enums.EstadoPublicacion;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.services.PropiedadService;
import tuti.desi.services.PublicacionService;

@Controller
@RequestMapping("/publicacionesEditar")
public class PublicacionEditarController {

    private final PublicacionService servicio;
    private final PropiedadService propiedadService;

    public PublicacionEditarController(PublicacionService servicio, PropiedadService propiedadService) {
        this.servicio = servicio;
        this.propiedadService = propiedadService;
    }

    @GetMapping({"/", "", "/{id}"})
    public String preparaForm(Model modelo, @PathVariable Optional<Long> id) throws Excepcion {
        PublicacionForm formBean = new PublicacionForm();
        
        if (id.isPresent()) {
            Publicacion entity = servicio.getById(id.get());
            formBean.setId(entity.getId());
            formBean.setPropiedadId(entity.getPropiedad().getId());
            formBean.setPrecioMensual(entity.getPrecioMensual());
            formBean.setEstadoPublicacion(entity.getEstadoPublicacion());
            formBean.setCondiciones(entity.getCondiciones());
            formBean.setDescripcion(entity.getDescripcion());
            formBean.setFechaPublicacion(entity.getFechaPublicacion());
        }
        
        modelo.addAttribute("formBean", formBean);
        modelo.addAttribute("propiedades", propiedadService.getAllActivas());
        modelo.addAttribute("estados", EstadoPublicacion.values());
        
        return "publicacionesEditar";
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws Excepcion {
        servicio.deleteById(id);
        return "redirect:/publicacionesBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute PublicacionForm formBean,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {

        if (action.equals("actionAceptar")) {
            try {
                Publicacion publicacion = new Publicacion();
                if (formBean.getId() != null) {
                    publicacion = servicio.getById(formBean.getId());
                }
                
                Propiedad propiedad = propiedadService.getById(formBean.getPropiedadId());
                publicacion.setPropiedad(propiedad);
                publicacion.setPrecioMensual(formBean.getPrecioMensual());
                publicacion.setEstadoPublicacion(formBean.getEstadoPublicacion());
                publicacion.setCondiciones(formBean.getCondiciones());
                publicacion.setDescripcion(formBean.getDescripcion());
                
                if (formBean.getId() == null) {
                    publicacion.setFechaPublicacion(LocalDate.now());
                }
                
                return "redirect:/publicacionesBuscar";
                
            } catch (Exception e) {
                modelo.addAttribute("error", e.getMessage());
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("propiedades", propiedadService.getAllActivas());
                modelo.addAttribute("estados", EstadoPublicacion.values());
                return "publicacionesEditar";
            }
            
        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/publicacionesBuscar";
        }
        
        return "redirect:/";
    }
}