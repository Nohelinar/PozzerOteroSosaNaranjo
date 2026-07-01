package tuti.desi.controller.facturas;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tuti.desi.entities.Factura;
import tuti.desi.entities.enums.EstadoFactura;
import tuti.desi.services.ContratoService;
import tuti.desi.services.FacturaService;
import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Controller
@RequestMapping("/facturasBuscar")
public class FacturaBuscarController {

    private final FacturaService servicio;
    private final ContratoService contratoService;
    private final PropiedadService propiedadService;
    private final PersonaService personaService;

    public FacturaBuscarController(FacturaService servicio, ContratoService contratoService, PropiedadService propiedadService, PersonaService personaService) {
        this.servicio = servicio;
        this.contratoService = contratoService;
        this.propiedadService = propiedadService;
        this.personaService = personaService;
    }

    @GetMapping
    public String preparaForm(Model modelo) {
        modelo.addAttribute("formBean", new FacturaBuscarForm());
        modelo.addAttribute("contratos", contratoService.getAllActivos());
        modelo.addAttribute("propiedades", propiedadService.getAllActivas());
        modelo.addAttribute("personas", personaService.getAllActivas());
        modelo.addAttribute("estados", EstadoFactura.values());
        return "facturasBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute FacturaBuscarForm formBean,
            ModelMap modelo,
            @RequestParam String action) {

        if (action.equals("actionBuscar")) {
            List<Factura> resultados = servicio.filter(
                formBean.getContratoId(),
                formBean.getPropiedadId(),
                formBean.getInquilinoId(),
                formBean.getEstado(),
                formBean.getFechaVencimientoDesde(),
                formBean.getFechaVencimientoHasta()
            );
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("formBean", formBean);
            modelo.addAttribute("contratos", contratoService.getAllActivos());
            modelo.addAttribute("propiedades", propiedadService.getAllActivas());
            modelo.addAttribute("personas", personaService.getAllActivas());
            modelo.addAttribute("estados", EstadoFactura.values());
            return "facturasBuscar";

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/facturasBuscar";

        } else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/facturasEditar";
        }

        return "redirect:/";
    }
}