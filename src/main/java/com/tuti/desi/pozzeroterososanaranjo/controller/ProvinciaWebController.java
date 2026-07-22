package com.tuti.desi.pozzeroterososanaranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tuti.desi.pozzeroterososanaranjo.entity.Provincia;
import com.tuti.desi.pozzeroterososanaranjo.service.ProvinciaService;

@Controller
public class ProvinciaWebController {

	@Autowired
	private ProvinciaService provinciaService;

	@GetMapping("/provincias/listado")
	public String listar(Model model) {
		model.addAttribute("provincias", provinciaService.listar());
		return "provincia/listado";
	}

	@GetMapping("/provincias/alta")
	public String mostrarAlta(Model model) {
		model.addAttribute("provincia", new Provincia());
		return "provincia/alta";
	}

	@PostMapping("/provincias/alta")
	public String alta(@ModelAttribute Provincia provincia, Model model) {
		try {
			provinciaService.guardar(provincia);
			return "redirect:/provincias/listado";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("provincia", provincia);
			return "provincia/alta";
		}
	}
}
