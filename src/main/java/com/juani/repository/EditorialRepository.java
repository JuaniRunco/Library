package com.juani.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.juani.entity.Editorial;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, String> {
	
	//consulta de la editorial por nombre
	@Query("Select e From Editorial e WHERE e.nombre= :nombre") 
	public List<Editorial> findAll(@Param("nombre") String nombre);
		
	//consulta de la editorial por id
	@Query("Select e From Editorial e WHERE e.id= :id")
	public Editorial findByid(@Param("id") String id) ;

	@Query("SELECT e FROM Editorial e")
    public List<Editorial> ListarEditoriales();
}
