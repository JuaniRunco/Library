package com.juani.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juani.entity.Foto;

public interface FotoRepository extends JpaRepository<Foto,String> {
	
}
