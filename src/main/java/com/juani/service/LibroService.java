package com.juani.service;

import com.juani.entity.Libro;

public interface LibroService {
	
public Iterable <Libro> findAll();
	
	public Libro findById(String id);
	
	public Libro save(Libro libro);
	
	public void deleteById(String id); 

}
