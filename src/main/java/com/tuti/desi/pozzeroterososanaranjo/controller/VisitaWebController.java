package com.tuti.desi.pozzeroterososanaranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Visita;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoVisita;
import com.tuti.desi.pozzeroterososanaranjo.service.VisitaService;

@Controller
public class VisitaWebController {

	@Autowired
	private VisitaService visitaService;

	@GetMapping("/visitas/listado")
	public String listar(@RequestParam(required = false) Long publicacionId, Model model) {
		model.addAttribute("visitas", visitaService.listarPorPublicacion(publicacionId));
		model.addAttribute("publicacionId", publicacionId);
		model.addAttribute("publicaciones", visitaService.encontrarPublicaciones());
		return "visita/listado";
	}

	@GetMapping("/visitas/alta")
	public String mostrarAlta(Model model) {
		model.addAttribute("visita", new Visita());
		model.addAttribute("publicaciones", visitaService.encontrarPublicaciones());
		model.addAttribute("estados", EstadoVisita.values());
		return "visita/alta";
	}

	@PostMapping("/visitas/alta")
	public String alta(@ModelAttribute Visita visita, Model model) {
		try {
			visitaService.altaVisita(visita);
			return "redirect:/visitas/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("visita", visita);
			model.addAttribute("publicaciones", visitaService.encontrarPublicaciones());
			model.addAttribute("estados", EstadoVisita.values());
			return "visita/alta";
		}
	}

	@GetMapping("/visitas/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		try {
			model.addAttribute("visita", visitaService.buscarPorId(id));
			model.addAttribute("estados", EstadoVisita.values());
			return "visita/editar";
		} catch (RuntimeException e) {
			model.addAttribute("visitas", visitaService.listarPorPublicacion(null));
			model.addAttribute("publicaciones", visitaService.encontrarPublicaciones());
			model.addAttribute("error", e.getMessage());
			return "visita/listado";
		}
	}

	@PostMapping("/visitas/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Visita visita, Model model) {
		try {
			visitaService.modificarVisita(id, visita);
			return "redirect:/visitas/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("visita", visita);
			model.addAttribute("estados", EstadoVisita.values());
			return "visita/editar";
		}
	}
}
