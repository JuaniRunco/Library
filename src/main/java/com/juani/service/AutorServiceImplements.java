package com.juani.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juani.entity.Autor;
import com.juani.exceptions.ExceptionService;
import com.juani.repository.AutorRepository;

@Service
public class AutorServiceImplements implements AutorService {

	@Autowired
	private AutorRepository autorRepository;

	@Override
	@Transactional
	public Iterable<Autor> findAll() {

		return autorRepository.findAll();
	}

	@Transactional
	public Autor findById(String id) {

		return autorRepository.findByid(id);
	}

	@Override
	@Transactional
	public Autor save(Autor autor) {

		return autorRepository.save(autor);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		autorRepository.deleteById(id);

	}

	@Transactional
	public void registrarAutor(String nombre, boolean alta) throws ExceptionService {

		validarDato(nombre);
		Autor autor = new Autor();
		autor.setNombre(nombre);
		autor.setAlta(alta);

		autorRepository.save(autor);
	}

	@Transactional
	public void modificarAutor(String id, String nombre) throws ExceptionService {

		validarDato(nombre);

		Autor autor = autorRepository.findByid(id);

		autor.setNombre(nombre);

		autorRepository.save(autor);
	}

	@Transactional 
    public void eliminarAutor(String id){
        Autor autor = autorRepository.findByid(id);
        autorRepository.delete(autor);
    }

	@Transactional
	public void darBajaAutor(String id) throws ExceptionService {
		Autor autor = autorRepository.findByid(id);
		autor.setAlta(false);
		autorRepository.save(autor);
	}

	@Transactional
	public void darAltaAutor(String id) throws ExceptionService {
		Autor autor = autorRepository.findByid(id);
		autor.setAlta(true);
	}

	@Transactional
	public List<Autor> listarAutores() {
		List<Autor> autores = autorRepository.ListarAutores();
		return autores;
	}

	private void validarDato(String nombre) throws ExceptionService {

		if (nombre == null || nombre.isEmpty()) {
			throw new ExceptionService("Debe indicar el nombre del autor!");
		}
	}

}
