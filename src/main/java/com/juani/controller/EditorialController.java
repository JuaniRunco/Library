package com.juani.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.juani.entity.Editorial;
import com.juani.exceptions.ExceptionService;
import com.juani.service.EditorialServiceImplements;

@Controller
@RequestMapping("/editorial")

public class EditorialController {

	@Autowired
	private EditorialServiceImplements editorialServiceImplements;

	@GetMapping
	public String editorial() {
		return "editorial";
	}

	@GetMapping("/registro")
	public String formulario() {
		return "form-editorial";
	}

	@PostMapping("/registro")
	public String guardar(ModelMap modelo, @RequestParam String nombre) {
		try {
			editorialServiceImplements.registrarEditorial(nombre, true);
			modelo.put("exito", "Registro exitoso");
			return "form-editorial";
		} catch (ExceptionService e) {
			modelo.put("error", e.getMessage());
			return "form-editorial";
		}
	}

	@GetMapping("/lista")
	public String lista(ModelMap modelo) {
		List<Editorial> editoriales = editorialServiceImplements.listarEditoriales();
		modelo.addAttribute("editoriales", editoriales);
		return "lista-editoriales";
	}

	@GetMapping("/modificar/{id}")
	public String modificar(ModelMap modelo, @PathVariable String id) {
		Editorial editorial = editorialServiceImplements.findById(id);
		modelo.addAttribute("editorial", editorial);
		return "modificar-editorial";
	}

	@PostMapping("/modificar/{id}")
	public String modificar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
		try {
			editorialServiceImplements.modificarEditorial(id, nombre);
			modelo.put("exito", "");
			return "redirect:/editorial/lista";
		} catch (Exception e) {
			modelo.put("error", e.getMessage());
			return "redirect:/editorial/modificar/{id}";
		}

	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(ModelMap modelo,@PathVariable String id) {
		try {
			editorialServiceImplements.deleteById(id);
			lista(modelo);
			modelo.put("exito", "Eliminacion Exitosa!");
			return "lista-editoriales";
		} catch (Exception e) {			
			lista(modelo);
			modelo.put("error", "Error al Eliminar!");
			return "lista-editoriales";
		}
	}

	@GetMapping("/baja/{id}")
	public String baja(@PathVariable String id) {
		try {
			editorialServiceImplements.darBajaEditorial(id);
			return "redirect:/editorial/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}

	@GetMapping("/alta/{id}")
	public String alta(@PathVariable String id) {
		try {
			editorialServiceImplements.darAltaAutor(id);
			return "redirect:/editorial/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}
}
