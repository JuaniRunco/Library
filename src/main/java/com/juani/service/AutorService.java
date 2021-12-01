package com.juani.service;

import com.juani.entity.Autor;

public interface AutorService {
	
	public Iterable<Autor> findAll();
	
	public Autor findById(String id);
	
	public Autor save(Autor autor);
	
	public void deleteById(String id);
	
}
