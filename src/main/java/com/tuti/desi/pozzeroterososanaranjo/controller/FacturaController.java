package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Factura;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoFactura;
import com.tuti.desi.pozzeroterososanaranjo.service.FacturaService;

@Controller
public class FacturaController {

	@Autowired
	private FacturaService facturaService;

	@GetMapping("/facturas/listado")
	public String listarFacturas(@RequestParam(required = false) Long contratoId,
			@RequestParam(required = false) Long propiedadId, @RequestParam(required = false) Long inquilinoId,
			@RequestParam(required = false) EstadoFactura estado,
			@RequestParam(required = false) LocalDate vencimientoDesde,
			@RequestParam(required = false) LocalDate vencimientoHasta, Model model) {

		List<Factura> listadoFacturas = facturaService.listarFacturas(contratoId, propiedadId, inquilinoId, estado,
				vencimientoDesde, vencimientoHasta);

		model.addAttribute("facturas", listadoFacturas);
		model.addAttribute("estado", estado);
		model.addAttribute("contratos", facturaService.encontrarContratosActivos());
		model.addAttribute("contratoId", contratoId);
		model.addAttribute("propiedadId", propiedadId);
		model.addAttribute("inquilinoId", inquilinoId);
		model.addAttribute("vencimientoDesde", vencimientoDesde);
		model.addAttribute("vencimientoHasta", vencimientoHasta);

		return "factura/listado";
	}

	@GetMapping("/facturas/alta")
	public String mostrarAltaFactura(Model model) {

		model.addAttribute("factura", new Factura());
		model.addAttribute("contratos", facturaService.encontrarContratosActivos());

		return "factura/alta";
	}

	@PostMapping("/facturas/alta")
	public String altaFactura(@ModelAttribute Factura factura, Model model) {

		try {
			facturaService.altaFactura(factura);
			return "redirect:/facturas/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("factura", factura);
			model.addAttribute("contratos", facturaService.encontrarContratosActivos());

			return "factura/alta";
		}
	}

	@PostMapping("/facturas/eliminar/{id}")
	public String eliminarFactura(@PathVariable Long id, Model model) {

		List<Factura> listadoFacturas = facturaService.listarFacturas(null, null, null, null, null, null);

		try {
			facturaService.eliminarFactura(id);
			return "redirect:/facturas/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("id", id);
			model.addAttribute("facturas", listadoFacturas);

			return "factura/listado";
		}
	}

	@GetMapping("/facturas/editar/{id}")
	public String editarFactura(@PathVariable Long id, Model model) {

		Factura facturaAEditar = facturaService.buscarPorId(id);
		model.addAttribute("factura", facturaAEditar);

		return "factura/editar";
	}

	@PostMapping("/facturas/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Factura factura, Model model) {

		try {
			facturaService.modificarFactura(id, factura);
			return "redirect:/facturas/listado";

		} catch (RuntimeException e) {
			Factura facturaExistente = facturaService.buscarPorId(id);
			factura.setContrato(facturaExistente.getContrato());

			model.addAttribute("error", e.getMessage());
			model.addAttribute("factura", factura);

			return "factura/editar";
		}
	}
}