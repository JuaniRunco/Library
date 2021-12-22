package com.juani.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Cliente;
import com.juani.exceptions.ExceptionService;
import com.juani.service.ClienteServiceImplements;

@Controller
public class PortalController {

	@Autowired
	private ClienteServiceImplements clienteServiceImplements;

	@GetMapping("/")
	public String index(ModelMap modelo) {
		List<Cliente> clientesActivos = clienteServiceImplements.todosLosClientes();
		modelo.addAttribute("clientes", clientesActivos);
		return "index";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
	@GetMapping("/inicio")
	public String inicio() {
		return "inicio";
	}

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
			ModelMap model) {
		if (error != null) {
			model.put("error", "Usuario o clave incorrectos");
		}
		if (logout != null) {
			model.put("logout", "Ha salido correctamente.");
		}
		return "login";
	}

	@GetMapping("/registro")
	public String registro() {
		return "registro";
	}

	@PostMapping("/registro")
	public String registro(ModelMap modelo, MultipartFile archivo, @RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido, 
			@RequestParam String telefono, @RequestParam String contraseña1, @RequestParam String contraseña2) {
			try {
				clienteServiceImplements.registrar(archivo, documento, nombre, apellido, telefono, contraseña1, contraseña2);				
			} catch (ExceptionService e) {
				modelo.put("error", e.getMessage());
				modelo.put("documento", documento);
				modelo.put("nombre", nombre);
				modelo.put("apellido", apellido);
				modelo.put("telefono", telefono);
				modelo.put("contraseña1", contraseña1);
				modelo.put("contraseña2", contraseña2);
				return "form-cliente";
			}
			modelo.put("titulo", "Bienvenido al Book Store ");
	        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria y te podes loguear ");
			return "exito";
	}
	
}
