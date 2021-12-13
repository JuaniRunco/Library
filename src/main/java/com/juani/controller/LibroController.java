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
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Autor;
import com.juani.entity.Editorial;
import com.juani.entity.Libro;
import com.juani.repository.AutorRepository;
import com.juani.repository.EditorialRepository;
import com.juani.service.LibroServiceImplements;

@Controller
@RequestMapping("/libro")
@PreAuthorize("hasAnyRole('ADMIN')")
public class LibroController {
	
	@Autowired
	private LibroServiceImplements libroServiceImplements;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private EditorialRepository editorialRepository;
	
	@GetMapping
	public String libro() {
		return "libro";
	}
	
	@GetMapping ("/registro")
	public String formulario(ModelMap modelo) {
		List<Autor> autores=autorRepository.findAll();
		List<Editorial> editoriales=editorialRepository.findAll();
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
		return "form-libro";
	}
	
	@PostMapping ("/registro")
	public String formulario(ModelMap modelo,MultipartFile archivo,@RequestParam Long isbn,@RequestParam String Titulo,@RequestParam Integer anio,@RequestParam Integer ejemplares,
			@RequestParam Integer ejemplaresPrestados, @RequestParam String idAutor, @RequestParam String idEditorial ) {
		List<Autor> autores=autorRepository.findAll();
		List<Editorial> editoriales=editorialRepository.findAll();
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
		try {
			libroServiceImplements.crearLibro(archivo, idAutor, idEditorial, isbn, Titulo, anio, ejemplares, ejemplaresPrestados);
			modelo.put("exito", "El libro se creo exitosamente!");
			
		} catch (Exception e) {
			//me falta validar el isbn
			modelo.put("error", e.getMessage());			
		}
		return "form-libro";
	}
	
	@GetMapping("/lista")
	public String lista(ModelMap modelo) {
		List<Libro> libros = libroServiceImplements.listarLibros();
		modelo.addAttribute("libros", libros);
		return "lista-libros";
	}
	
	@GetMapping("/modificar/{id}")
	public String modificar(ModelMap modelo, @PathVariable String id ) {
		List<Autor> autores=autorRepository.findAll();
		List<Editorial> editoriales=editorialRepository.findAll();
		Libro libro = libroServiceImplements.findById(id);
		modelo.addAttribute("libro", libro);
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
		return "modificar-libro";
	}
	
	@PostMapping("/modificar/{id}")
	public String modificar(ModelMap modelo, @PathVariable String id,MultipartFile archivo,@RequestParam Long isbn,@RequestParam String Titulo,@RequestParam Integer anio,
			@RequestParam Integer ejemplares,@RequestParam Integer ejemplaresPrestados,@RequestParam String idAutor, @RequestParam String idEditorial) {
		try {
			libroServiceImplements.modificarLibro(archivo, idAutor, idEditorial, id, isbn, Titulo, anio, ejemplares, ejemplaresPrestados);
			modelo.put("exito", "");
			return "lista-libros";
		} catch (Exception e) {
			modelo.put("error", e.getMessage());
			return "redirect:/libro/modificar/{id}";
		}

	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(ModelMap modelo,@PathVariable String id) {
		try {
			libroServiceImplements.deleteById(id);
			lista(modelo);
			modelo.put("exito", "Eliminacion Exitosa!");
			return "lista-libros";
		} catch (Exception e) {			
			lista(modelo);
			modelo.put("error", "Error al Eliminar!");
			return "lista-libros";
		}
	}

	@GetMapping("/baja/{id}")
	public String baja(@PathVariable String id) {
		try {
			libroServiceImplements.darBajaLibro(id);
			return "redirect:/libro/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}

	@GetMapping("/alta/{id}")
	public String alta(@PathVariable String id) {
		try {
			libroServiceImplements.darAltaLibro(id);
			return "redirect:/libro/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}
}
