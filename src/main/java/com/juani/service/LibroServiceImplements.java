package com.juani.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Autor;
import com.juani.entity.Editorial;
import com.juani.entity.Foto;
import com.juani.entity.Libro;
import com.juani.exceptions.ExceptionService;
import com.juani.repository.AutorRepository;
import com.juani.repository.EditorialRepository;
import com.juani.repository.LibroRepository;

@Service
public class LibroServiceImplements implements LibroService {
	// Ver Capa de Sv 5 a 7
	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private EditorialRepository editorialRepository;

	@Autowired
	private FotoServiceImplements fotoService;

	@Override
	@Transactional
	public Iterable<Libro> findAll() {

		return libroRepository.findAll();
	}

	@Override
	@Transactional
	public Libro findById(String id) {

		return libroRepository.findByid(id);
	}

	@Override
	@Transactional
	public Libro save(Libro libro) {

		return libroRepository.save(libro);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		libroRepository.deleteById(id);

	}

	@Transactional
	public void crearLibro(MultipartFile archivo, String idAutor, String idEditorial, Long isbn, String titulo,
			Integer anio, Integer ejemplares, Integer ejemplaresPrestados) throws ExceptionService {

		Autor autor = autorRepository.findById(idAutor).get();
		Editorial editorial = editorialRepository.findById(idEditorial).get();

		validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados);

		Libro libro = new Libro();
		libro.setTitulo(titulo);
		libro.setIsbn(isbn);
		libro.setAnio(anio);
		libro.setEjemplares(ejemplares);
		libro.setEjemplaresPrestados(ejemplaresPrestados);
		libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
		libro.setAlta(true);
		libro.setAutor(autor);
		libro.setEditorial(editorial);

		Foto foto = fotoService.guardarFoto(archivo);
		libro.setFoto(foto);
		libroRepository.save(libro);
	}

	@Transactional
	public void modificarLibro(MultipartFile archivo, String idAutor, String idEditorial, String id, Long isbn,
			String titulo, Integer anio, Integer ejemplares,Integer ejemplaresPrestados) throws ExceptionService {

		validar(isbn, titulo, anio, ejemplares,ejemplaresPrestados);

		Optional<Libro> respuesta = libroRepository.findById(id);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			Autor autor = autorRepository.findById(idAutor).get();
			Editorial editorial = editorialRepository.findById(idEditorial).get();

			libro.setIsbn(isbn);
			libro.setTitulo(titulo);
			libro.setIsbn(isbn);
			libro.setAnio(anio);
			libro.setEjemplares(ejemplares);
			libro.setEjemplaresPrestados(ejemplaresPrestados);
			libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);
			libro.setAlta(true);
			libro.setAutor(autor);
			libro.setEditorial(editorial);
			//Si el archivo contiene foto  
			if (!archivo.isEmpty()) {
				String idFoto = null;
				//Se le agrega al idFoto la foto que ya tenia 
				if (libro.getFoto() != null) {
					idFoto = libro.getFoto().getId();
				}
				//Caso contrario la actualiza
				Foto foto = fotoService.actualizarFoto(idFoto, archivo);
				libro.setFoto(foto);

				libroRepository.save(libro);
			}
		}
	}

	@Transactional
	public void eliminarLibro(String idAutor, String idEditorial, String idLibro) throws ExceptionService {
		Optional<Libro> respuesta = libroRepository.findById(idLibro);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			if (libro.getAutor().getId().equals(idAutor)) {
				if (libro.getEditorial().getId().equals(idEditorial)) {
					libro.setAlta(false);
					libroRepository.save(libro);
				}
			}
		}
	}
	
	@Transactional
	public void darBajaLibro(String id) throws ExceptionService {
		Optional<Libro> respuesta = libroRepository.findById(id);
		if (respuesta.isPresent()) {
			Libro libro=respuesta.get();
			libro.setAlta(false);
			libroRepository.save(libro); 
		} else {
			throw new ExceptionService("No se pudo dar de baja al libro solicitado"); 	 	
		}
	}
	
	@Transactional
	public void darAltaLibro(String id) throws ExceptionService {
		Optional<Libro> respuesta = libroRepository.findById(id);
		if (respuesta.isPresent()) {
			Libro libro=respuesta.get();
			libro.setAlta(true);
			libroRepository.save(libro);
		} else {
			throw new ExceptionService("No se pudo dar de baja el libro solicitada"); 	 	
		}
	}

	@Transactional
	public List<Libro> listarLibros() {
		List<Libro> libros = libroRepository.listarLibros();
		return libros;
	}

	public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados)
			throws ExceptionService {

		if (titulo == null || titulo.isEmpty()) {
			throw new ExceptionService("Indique el nombre del titulo del libro!");
		}
		if (isbn == null) {
			throw new ExceptionService("Indique el isbn del libro");

		}
		if (anio == null) {
			throw new ExceptionService("Indique el año del libro");

		}
		if (ejemplares < 0 || ejemplares == null) {
			throw new ExceptionService(
					"Revise la cantidad de ejemplares del libro, puede que se haya ingresado un dato erroneo!");
		}
		if (ejemplaresPrestados < 0 || ejemplaresPrestados == null) {
			throw new ExceptionService(
					"Revise la cantidad de ejemplares prestados del libro, puede que se haya ingresado un dato erroneo!");
		}

		if (ejemplaresPrestados >= ejemplares) {
			throw new ExceptionService("No queda ejemplares de ese libro para prestar");
		}

	}

}
