package com.juani.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.juani.entity.Autor;
import com.juani.exceptions.ExceptionService;
import com.juani.service.AutorServiceImplements;

@Controller
@RequestMapping("/autor")
public class AutorController {

	@Autowired
	private AutorServiceImplements autorServiceImplements;

	@GetMapping
	public String autor() {
		return "autor";
	}

	@GetMapping("/registro")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String formulario() {
		return "form-autor";
	}

	@PostMapping("/registro")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String guardar(ModelMap modelo, @RequestParam String nombre) {
		try {
			autorServiceImplements.registrarAutor(nombre, true);
			modelo.put("exito", "Registro exitoso");
			return "form-autor";
		} catch (ExceptionService e) {
			modelo.put("error", e.getMessage());
			return "form-autor";
		}
	}

	@GetMapping("/lista")
	public String lista(ModelMap modelo) {
		List<Autor> autores = autorServiceImplements.listarAutores();
		modelo.addAttribute("autores", autores);
		return "lista-autores";
	}

	@GetMapping("/modificar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String modificar(ModelMap modelo, @PathVariable String id) {
		Autor autor = autorServiceImplements.findById(id);
		modelo.addAttribute("autor", autor);
		return "modificar-autor";
	}

	@PostMapping("/modificar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String modificar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
		try {

			autorServiceImplements.modificarAutor(id, nombre);
			modelo.put("exito", "");
			return "redirect:/autor/lista";
		} catch (Exception e) {
			modelo.put("error", e.getMessage());
			return "redirect:/autor/modificar/{id}";
		}

	}

	@GetMapping("/eliminar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String eliminar(ModelMap modelo, @PathVariable String id) {
		try {
			autorServiceImplements.deleteById(id);
			lista(modelo);
			modelo.put("exito", "Eliminacion Exitosa!");
			return "lista-autores";
		} catch (Exception e) {
			lista(modelo);
			modelo.put("error", "Error al Eliminar!");
			return "lista-autores";
		}
	}

	@GetMapping("/baja/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String baja(@PathVariable String id) {
		try {
			autorServiceImplements.darBajaAutor(id);
			return "redirect:/autor/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}

	@GetMapping("/alta/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String alta(@PathVariable String id) {
		try {
			autorServiceImplements.darAltaAutor(id);
			return "redirect:/autor/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}
}
