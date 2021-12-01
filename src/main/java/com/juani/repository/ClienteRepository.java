package com.juani.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.juani.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String>{
	
	@Query("SELECT c FROM Cliente c ")
	public List<Cliente>listarCliente();
	
	@Query("SELECT c FROM Cliente c WHERE c.alta IS NOT FALSE")
	public List<Cliente>listarclienteDeAlta();
	
	@Query("SELECT c FROM Cliente c WHERE c.id= :id")
	public Cliente buscarPorId(@Param("id") String id);
	
	@Query("Select a From Cliente a WHERE a.id= :id")
	public void deleteById(String id);
	
	@Query("SELECT c FROM Cliente c WHERE c.nombre= :nombre")
	public Cliente buscarPorNombre(@Param("nombre") String nombre);
}
