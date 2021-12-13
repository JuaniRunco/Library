package com.juani.service;

import com.juani.entity.Editorial;

public interface EditorialService {
	
	public Iterable <Editorial> findAll();
	
	public Editorial findById(String id);
	
	public Editorial save(Editorial editorial);
	
	public void deleteById(String id); 
}
