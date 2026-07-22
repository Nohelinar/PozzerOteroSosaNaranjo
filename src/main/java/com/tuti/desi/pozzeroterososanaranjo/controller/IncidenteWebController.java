package com.tuti.desi.pozzeroterososanaranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Incidente;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoIncidente;
import com.tuti.desi.pozzeroterososanaranjo.service.IncidenteService;

@Controller
public class IncidenteWebController {

	@Autowired
	private IncidenteService incidenteService;

	@GetMapping("/incidentes/listado")
	public String listar(
			@RequestParam(required = false) Long propiedadId,
			@RequestParam(required = false) EstadoIncidente estado,
			Model model) {

		model.addAttribute("incidentes", incidenteService.listarConFiltros(propiedadId, estado));
		model.addAttribute("propiedadId", propiedadId);
		model.addAttribute("estado", estado);
		model.addAttribute("estados", EstadoIncidente.values());
		model.addAttribute("propiedades", incidenteService.encontrarPropiedades());

		return "incidente/listado";
	}

	@GetMapping("/incidentes/alta")
	public String mostrarAlta(Model model) {
		model.addAttribute("incidente", new Incidente());
		cargarDatosFormulario(model);
		return "incidente/alta";
	}

	@PostMapping("/incidentes/alta")
	public String alta(@ModelAttribute Incidente incidente, Model model) {
		try {
			incidenteService.altaIncidente(incidente);
			return "redirect:/incidentes/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("incidente", incidente);
			cargarDatosFormulario(model);
			return "incidente/alta";
		}
	}

	@GetMapping("/incidentes/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		try {
			model.addAttribute("incidente", incidenteService.buscarPorId(id));
			cargarDatosFormulario(model);
			return "incidente/editar";
		} catch (RuntimeException e) {
			model.addAttribute("incidentes", incidenteService.listarConFiltros(null, null));
			model.addAttribute("estados", EstadoIncidente.values());
			model.addAttribute("propiedades", incidenteService.encontrarPropiedades());
			model.addAttribute("error", e.getMessage());
			return "incidente/listado";
		}
	}

	@PostMapping("/incidentes/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Incidente incidente, Model model) {
		try {
			incidenteService.modificarIncidente(id, incidente);
			return "redirect:/incidentes/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("incidente", incidente);
			cargarDatosFormulario(model);
			return "incidente/editar";
		}
	}

	@PostMapping("/incidentes/eliminar/{id}")
	public String eliminar(@PathVariable Long id, Model model) {
		try {
			incidenteService.eliminarIncidente(id);
			return "redirect:/incidentes/listado";
		} catch (RuntimeException e) {
			model.addAttribute("incidentes", incidenteService.listarConFiltros(null, null));
			model.addAttribute("estados", EstadoIncidente.values());
			model.addAttribute("propiedades", incidenteService.encontrarPropiedades());
			model.addAttribute("error", e.getMessage());
			return "incidente/listado";
		}
	}

	private void cargarDatosFormulario(Model model) {
		model.addAttribute("propiedades", incidenteService.encontrarPropiedades());
		model.addAttribute("categorias", com.tuti.desi.pozzeroterososanaranjo.enums.CategoriaIncidente.values());
		model.addAttribute("prioridades", com.tuti.desi.pozzeroterososanaranjo.enums.PrioridadIncidente.values());
		model.addAttribute("estados", EstadoIncidente.values());
	}
}
