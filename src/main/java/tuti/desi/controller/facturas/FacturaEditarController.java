package tuti.desi.controller.facturas;

import java.time.LocalDate;
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
import tuti.desi.entities.Factura;
import tuti.desi.entities.Contrato;
import tuti.desi.entities.enums.EstadoFactura;
import tuti.desi.entities.enums.MedioPago;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.services.ContratoService;
import tuti.desi.services.FacturaService;

@Controller
@RequestMapping("/facturasEditar")
public class FacturaEditarController {

    private final FacturaService servicio;
    private final ContratoService contratoService;

    public FacturaEditarController(FacturaService servicio, ContratoService contratoService) {
        this.servicio = servicio;
        this.contratoService = contratoService;
    }

    @GetMapping({"/", "", "/{id}"})
    public String preparaForm(Model modelo, @PathVariable Optional<Long> id) throws Excepcion {
        Factura formBean = new Factura();

        if (id.isPresent()) {
            formBean = servicio.getById(id.get());
        }

        modelo.addAttribute("formBean", formBean);
        modelo.addAttribute("contratos", contratoService.getAllActivos());
        modelo.addAttribute("estados", EstadoFactura.values());
        modelo.addAttribute("mediosPago", MedioPago.values());

        return "facturasEditar";
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws Excepcion {
        servicio.deleteById(id);
        return "redirect:/facturasBuscar";
    }

    @PostMapping
    public String submit(
            @ModelAttribute @Valid Factura formBean,
            BindingResult result,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {

        if (action.equals("actionAceptar")) {
            if (result.hasErrors()) {
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("contratos", contratoService.getAllActivos());
                modelo.addAttribute("estados", EstadoFactura.values());
                modelo.addAttribute("mediosPago", MedioPago.values());
                return "facturasEditar";
            }

            try {
                // Cargar contrato
                Contrato contrato = contratoService.getById(formBean.getContrato().getId());
                formBean.setContrato(contrato);

                // Si es nueva, establecer estado por defecto
                if (formBean.getId() == null) {
                    formBean.setEstadoFactura(EstadoFactura.Pendiente);
                }

                // Si está pagada, validar datos de pago
                if (formBean.getEstadoFactura() == EstadoFactura.Pagada) {
                    if (formBean.getFechaPago() == null || formBean.getMedioPago() == null || formBean.getImportePagado() == null) {
                        throw new Excepcion("Para registrar una factura como pagada, debe completar los datos de pago");
                    }
                }

                Factura saved = servicio.save(formBean);

                // Registrar estado inicial
                if (formBean.getId() == null) {
                    servicio.cambiarEstado(saved, saved.getEstadoFactura());
                }

                return "redirect:/facturasBuscar";

            } catch (Excepcion e) {
                if (e.getAtributo() == null) {
                    ObjectError error = new ObjectError("globalError", e.getMessage());
                    result.addError(error);
                } else {
                    FieldError error = new FieldError("formBean", e.getAtributo(), e.getMessage());
                    result.addError(error);
                }
                modelo.addAttribute("formBean", formBean);
                modelo.addAttribute("contratos", contratoService.getAllActivos());
                modelo.addAttribute("estados", EstadoFactura.values());
                modelo.addAttribute("mediosPago", MedioPago.values());
                return "facturasEditar";
            }

        } else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/facturasBuscar";
        }

        return "redirect:/";
    }
}