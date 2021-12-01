package com.juani.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.juani.entity.Libro;
import com.juani.repository.FotoRepository;
import com.juani.service.LibroServiceImplements;

@Controller
@RequestMapping("/foto")
public class FotoController {
	
	@Autowired
	private FotoRepository fotoRepository;
	
	@Autowired
	private LibroServiceImplements libroServiceImplements;
	
	@GetMapping("/libro/{id}")
	public ResponseEntity<byte[]> fotoBook(@PathVariable String id){
		//buscando el libro dueño de la foto
		Libro libro;
		try {
			libro = libroServiceImplements.findById(id);
			//si el usuario no tiene foto
			if (libro.getFoto()== null) {
				throw new Exception("El libro no tiene foto para mostrar");
			}
			byte[] foto= libro.getFoto().getContenido();
			
			//creando las cabeceras necesarias para devolver una imagen
			//para indicarle al navegador que va a mostrar una foto
			HttpHeaders headers=new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			
			//devolviendo un response entity
			return new ResponseEntity<>(foto, headers , HttpStatus.OK);
		} catch (Exception ex) {
			Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}
}
