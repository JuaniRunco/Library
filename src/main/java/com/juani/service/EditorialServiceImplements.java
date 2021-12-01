package com.juani.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juani.entity.Editorial;
import com.juani.exceptions.ExceptionService;
import com.juani.repository.EditorialRepository;

@Service
public class EditorialServiceImplements implements EditorialService{
	
	@Autowired
	private EditorialRepository editorialRepository;
	
	@Override
	@Transactional
	public Iterable<Editorial> findAll() {
		
		return editorialRepository.findAll();
	}

	@Override
	@Transactional
	public Editorial findById(String id) {
		
		return editorialRepository.findByid(id);
	}

	@Override
	@Transactional
	public Editorial save(Editorial editorial) {

		return editorialRepository.save(editorial);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		editorialRepository.deleteById(id);
		
	}
	
	@Transactional
	public void registrarEditorial(String nombre, boolean alta) throws ExceptionService {
		
		validarDato(nombre);
		Editorial editorial=new Editorial();
		editorial.setNombre(nombre);
		editorial.setAlta(alta);
		
		editorialRepository.save(editorial); 
	}
	
	@Transactional
	public void modificarEditorial(String id,String nombre) throws ExceptionService {
		
		validarDato(nombre);
		
		Optional<Editorial> respuesta = editorialRepository.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial=respuesta.get();
			editorial.setNombre(nombre);
			
			editorialRepository.save(editorial); 
		} else {
			throw new ExceptionService("No se encontro la editorial solicitada"); 	 	
		}
		
	}
	
	@Transactional
	public void darBajaEditorial(String id) throws ExceptionService {
		Optional<Editorial> respuesta = editorialRepository.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial=respuesta.get();
			editorial.setAlta(false);
			editorialRepository.save(editorial); 
		} else {
			throw new ExceptionService("No se pudo dar de baja la editorial solicitada"); 	 	
		}
	}
	
	@Transactional
	public void darAltaAutor(String id) throws ExceptionService {
		Optional<Editorial> respuesta = editorialRepository.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial=respuesta.get();
			editorial.setAlta(true);
			editorialRepository.save(editorial); 
		} else {
			throw new ExceptionService("No se pudo dar de baja la editorial solicitada"); 	 	
		}
	}
	
	@Transactional
	public List<Editorial> listarEditoriales() {
		List<Editorial> editoriales = editorialRepository.ListarEditoriales();
		return editoriales;
	}

	private void validarDato(String nombre) throws ExceptionService {
		
		if (nombre==null||nombre.isEmpty()) {
			throw new ExceptionService("Debe indicar el nombre de la editorial!");
		}
	}
	
}
