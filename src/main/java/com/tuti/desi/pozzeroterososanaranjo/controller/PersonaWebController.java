package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Persona;
import com.tuti.desi.pozzeroterososanaranjo.service.CiudadService;
import com.tuti.desi.pozzeroterososanaranjo.service.PersonaService;

@Controller
public class PersonaWebController {

	@Autowired
	private PersonaService personaService;

	@Autowired
	private CiudadService ciudadService;

	@GetMapping("/personas/listado")
	public String listarPersonas(
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String apellido,
			Model model) {

		List<Persona> listadoPersonas = personaService.listarConFiltros(nombre, apellido);

		model.addAttribute("personas", listadoPersonas);
		model.addAttribute("nombre", nombre);
		model.addAttribute("apellido", apellido);

		return "persona/listado";
	}

	@GetMapping("/personas/alta")
	public String mostrarAltaPersona(Model model) {

		model.addAttribute("persona", new Persona());
		model.addAttribute("ciudades", ciudadService.listar());

		return "persona/alta";
	}

	@PostMapping("/personas/alta")
	public String altaPersona(@ModelAttribute Persona persona, Model model) {

		try {
			personaService.guardar(persona);
			return "redirect:/personas/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("persona", persona);
			model.addAttribute("ciudades", ciudadService.listar());

			return "persona/alta";
		}
	}

	@GetMapping("/personas/editar/{id}")
	public String editarPersona(@PathVariable Long id, Model model) {

		try {
			Persona personaAEditar = personaService.buscarPorId(id);
			model.addAttribute("persona", personaAEditar);
			model.addAttribute("ciudades", ciudadService.listar());

			return "persona/editar";

		} catch (RuntimeException e) {
			model.addAttribute("personas", personaService.listarConFiltros(null, null));
			model.addAttribute("error", e.getMessage());

			return "persona/listado";
		}
	}

	@PostMapping("/personas/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Persona persona, Model model) {

		try {
			personaService.modificar(id, persona);
			return "redirect:/personas/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("persona", persona);
			model.addAttribute("ciudades", ciudadService.listar());

			return "persona/editar";
		}
	}

	@PostMapping("/personas/eliminar/{id}")
	public String eliminarPersona(@PathVariable Long id, Model model) {

		try {
			personaService.eliminarLogicamente(id);
			return "redirect:/personas/listado";

		} catch (RuntimeException e) {
			model.addAttribute("personas", personaService.listarConFiltros(null, null));
			model.addAttribute("error", e.getMessage());

			return "persona/listado";
		}
	}
}
