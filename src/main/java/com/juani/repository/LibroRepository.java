package com.juani.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.juani.entity.Editorial;
import com.juani.entity.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

	//consulta del libro por titulo
	@Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
	public Libro buscarPorTitulo(@Param("titulo") String titulo);
	
	//consulta del libro por id
	@Query("Select l From Libro l WHERE l.id= :id")
	public Libro findByid(@Param("id") String id);

	@Query("SELECT l FROM Libro l")
	public List<Libro> listarLibros();
}
