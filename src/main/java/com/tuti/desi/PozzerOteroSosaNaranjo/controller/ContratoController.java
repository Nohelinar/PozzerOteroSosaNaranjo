package com.tuti.desi.PozzerOteroSosaNaranjo.controller;

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

import com.tuti.desi.PozzerOteroSosaNaranjo.entity.Contrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoContrato;
import com.tuti.desi.PozzerOteroSosaNaranjo.service.ContratoService;

@Controller
public class ContratoController {

	@Autowired
	private ContratoService contratoService;

	@GetMapping("/contratos/listado")
	public String listarContratos(@RequestParam(required = false) Long propiedadId,
			@RequestParam(required = false) Long inquilinoId, @RequestParam(required = false) EstadoContrato estado,
			@RequestParam(required = false) LocalDate fechaDesde, @RequestParam(required = false) LocalDate fechaHasta,
			Model model) {

		List<Contrato> listadoContratos = contratoService.listarContratos(propiedadId, inquilinoId, estado, fechaDesde,
				fechaHasta);

		model.addAttribute("contratos", listadoContratos);
		model.addAttribute("estado", estado);
		model.addAttribute("propiedades", contratoService.encontrarPropiedades());
		model.addAttribute("personas", contratoService.encontrarPersonas());
		model.addAttribute("propiedadId", propiedadId);
		model.addAttribute("inquilinoId", inquilinoId);
		model.addAttribute("fechaDesde", fechaDesde);
		model.addAttribute("fechaHasta", fechaHasta);

		return "contrato/listado";
	}

	@GetMapping("/contratos/alta")
	public String mostrarAltaContrato(Model model) {

		model.addAttribute("contrato", new Contrato());
		model.addAttribute("propiedades", contratoService.encontrarPropiedades());
		model.addAttribute("personas", contratoService.encontrarPersonas());

		return "contrato/alta";
	}

	@PostMapping("/contratos/alta")
	public String altaContrato(@ModelAttribute Contrato contrato, Model model) {

		try {
			contratoService.altaContrato(contrato);
			return "redirect:/contratos/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("contrato", contrato);
			model.addAttribute("propiedades", contratoService.encontrarPropiedades());
			model.addAttribute("personas", contratoService.encontrarPersonas());

			return "contrato/alta";
		}
	}

	@PostMapping("/contratos/eliminar/{id}")
	public String eliminarContrato(@PathVariable Long id, Model model) {

		List<Contrato> listadoContratos = contratoService.listarContratos(null, null, null, null, null);

		try {
			contratoService.eliminarContrato(id);
			return "redirect:/contratos/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("id", id);
			model.addAttribute("contratos", listadoContratos);

			return "contrato/listado";

		}
	}

	@GetMapping("/contratos/editar/{id}")
	public String editarContrato(@PathVariable Long id, Model model) {

		Contrato contratoAEditar = contratoService.buscarPorId(id);
		model.addAttribute("contrato", contratoAEditar);
		model.addAttribute("propiedades", contratoService.encontrarPropiedades());
		model.addAttribute("personas", contratoService.encontrarPersonas());

		return "contrato/editar";
	}

	@PostMapping("/contratos/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Contrato contrato, Model model) {

		try {
			contratoService.modificarContrato(id, contrato);
			return "redirect:/contratos/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("contrato", contrato);
			model.addAttribute("propiedades", contratoService.encontrarPropiedades());
			model.addAttribute("personas", contratoService.encontrarPersonas());
			return "contrato/editar";
		}
	}
}
