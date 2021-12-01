package com.juani.service;

import java.util.Optional;

import com.juani.entity.Prestamo;

public interface PrestamoService {
	
	public Iterable <Prestamo> findAll();
	
	public Optional<Prestamo> findById(String id);
	
	public Prestamo save(Prestamo prestamo);
	
	public void deleteById(String id); 
}
