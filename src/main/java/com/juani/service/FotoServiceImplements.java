package com.juani.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Foto;
import com.juani.exceptions.ExceptionService;
import com.juani.repository.FotoRepository;

@Service
public class FotoServiceImplements {
	
	@Autowired
	private FotoRepository fotoRepository;
	
	@Transactional
	public Foto guardarFoto(MultipartFile archivo) throws ExceptionService{
		
		if (archivo!=null) {
			try {
				Foto foto= new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepository.save(foto); 				
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			
		}		
		return null;	
	}
	
	@Transactional
	public Foto actualizarFoto(String idFoto, MultipartFile archivo) throws ExceptionService{
		
		if (archivo!=null) {
			try {				
				Foto foto= new Foto();
				
				if (idFoto!=null) {
					Optional<Foto> respuesta= fotoRepository.findById(idFoto);
					if (respuesta.isPresent()) {
						foto= respuesta.get();
					}
				}	
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepository.save(foto); 				
			} catch (Exception e) {			
				System.err.println(e.getMessage());
			}
			
		}		
		return null;	
	}
}
