package com.juani.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.juani.entity.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, String> {
	
	//consulta del autor por nombre
	@Query("Select a From Autor a WHERE a.nombre= :nombre") //PONER EN MAYUUUUUUSCUUULA EL OBJETO BOLUUUUDO (Autor no autor) 
	public List<Autor> findAll(@Param("nombre") String nombre);
	
	//consulta del autor por id
	@Query("Select a From Autor a WHERE a.id= :id")
	public Autor findByid(@Param("id") String id);
	
	@Query("SELECT c FROM Autor c")
    public List<Autor> ListarAutores();

}
