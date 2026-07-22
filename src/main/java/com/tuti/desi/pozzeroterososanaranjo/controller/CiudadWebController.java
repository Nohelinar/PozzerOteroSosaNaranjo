package com.tuti.desi.pozzeroterososanaranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tuti.desi.pozzeroterososanaranjo.entity.Ciudad;
import com.tuti.desi.pozzeroterososanaranjo.service.CiudadService;
import com.tuti.desi.pozzeroterososanaranjo.service.ProvinciaService;

@Controller
public class CiudadWebController {

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private ProvinciaService provinciaService;

	@GetMapping("/ciudades/listado")
	public String listar(Model model) {
		model.addAttribute("ciudades", ciudadService.listar());
		return "ciudad/listado";
	}

	@GetMapping("/ciudades/alta")
	public String mostrarAlta(Model model) {
		model.addAttribute("ciudad", new Ciudad());
		model.addAttribute("provincias", provinciaService.listar());
		return "ciudad/alta";
	}

	@PostMapping("/ciudades/alta")
	public String alta(@ModelAttribute Ciudad ciudad, Model model) {
		try {
			ciudadService.guardar(ciudad);
			return "redirect:/ciudades/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("ciudad", ciudad);
			model.addAttribute("provincias", provinciaService.listar());
			return "ciudad/alta";
		}
	}
}
