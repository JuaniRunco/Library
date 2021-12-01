package com.juani.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juani.entity.Prestamo;
import com.juani.repository.PrestamoRepository;

@Service
public class PrestamoServiceImplements implements PrestamoService {
	
	@Autowired
	private PrestamoRepository prestamoRepository;

	
	@Override
	@Transactional
	public Iterable<Prestamo> findAll() {
		
		return prestamoRepository.findAll();
	}
	
	@Override
	@Transactional
	public Optional<Prestamo> findById(String id) {

		return prestamoRepository.findById(id);
	}
	
	@Override
	@Transactional
	public Prestamo save(Prestamo prestamo) {

		return prestamoRepository.save(prestamo);
	}
	
	@Override
	@Transactional
	public void deleteById(String id) {
		
		prestamoRepository.deleteById(id);
		
	}
	
	
}
