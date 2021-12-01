package com.juani.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juani.entity.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {

}
