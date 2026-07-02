package com.tuti.desi.pozzeroterososanaranjo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuti.desi.pozzeroterososanaranjo.entity.Publicacion;
import com.tuti.desi.pozzeroterososanaranjo.enums.EstadoPublicacion;
import com.tuti.desi.pozzeroterososanaranjo.service.PublicacionService;

@Controller
public class PublicacionController {

	@Autowired
	private PublicacionService publicacionService;

	@GetMapping("/publicaciones/listado")
	public String listarPublicaciones(@RequestParam(required = false) Long propiedadId,
			@RequestParam(required = false) String ciudad, @RequestParam(required = false) EstadoPublicacion estado,
			@RequestParam(required = false) BigDecimal precioDesde, @RequestParam(required = false) BigDecimal precioHasta,
			Model model) {

		List<Publicacion> listadoPublicaciones = publicacionService.listarPublicaciones(propiedadId, ciudad, estado,
				precioDesde, precioHasta);

		model.addAttribute("publicaciones", listadoPublicaciones);
		model.addAttribute("estado", estado);
		model.addAttribute("propiedades", publicacionService.encontrarPropiedadesDisponibles());
		model.addAttribute("propiedadId", propiedadId);
		model.addAttribute("ciudad", ciudad);
		model.addAttribute("precioDesde", precioDesde);
		model.addAttribute("precioHasta", precioHasta);

		return "publicacion/listado";
	}

	@GetMapping("/publicaciones/alta")
	public String mostrarAltaPublicacion(Model model) {

		model.addAttribute("publicacion", new Publicacion());
		model.addAttribute("propiedades", publicacionService.encontrarPropiedadesDisponibles());

		return "publicacion/alta";
	}

	@PostMapping("/publicaciones/alta")
	public String altaPublicacion(@ModelAttribute Publicacion publicacion, Model model) {

		try {
			publicacionService.altaPublicacion(publicacion);
			return "redirect:/publicaciones/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("publicacion", publicacion);
			model.addAttribute("propiedades", publicacionService.encontrarPropiedadesDisponibles());

			return "publicacion/alta";
		}
	}

	@PostMapping("/publicaciones/eliminar/{id}")
	public String eliminarPublicacion(@PathVariable Long id, Model model) {

		List<Publicacion> listadoPublicaciones = publicacionService.listarPublicaciones(null, null, null, null, null);

		try {
			publicacionService.eliminarPublicacion(id);
			return "redirect:/publicaciones/listado";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("id", id);
			model.addAttribute("publicaciones", listadoPublicaciones);

			return "publicacion/listado";
		}
	}

	@GetMapping("/publicaciones/editar/{id}")
	public String editarPublicacion(@PathVariable Long id, Model model) {

		Publicacion publicacionAEditar = publicacionService.buscarPorId(id);
		model.addAttribute("publicacion", publicacionAEditar);

		return "publicacion/editar";
	}

	@PostMapping("/publicaciones/editar/{id}")
	public String guardarEdicion(@PathVariable Long id, @ModelAttribute Publicacion publicacion, Model model) {

		try {
			publicacionService.modificarPublicacion(id, publicacion);
			return "redirect:/publicaciones/listado";

		} catch (RuntimeException e) {
			Publicacion publicacionExistente = publicacionService.buscarPorId(id);
			publicacion.setPropiedad(publicacionExistente.getPropiedad());

			model.addAttribute("error", e.getMessage());
			model.addAttribute("publicacion", publicacion);

			return "publicacion/editar";
		}
	}
}