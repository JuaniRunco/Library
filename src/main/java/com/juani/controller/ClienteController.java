package com.juani.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Cliente;
import com.juani.exceptions.ExceptionService;
import com.juani.service.ClienteServiceImplements;

@Controller
public class ClienteController {

	@Autowired
	private ClienteServiceImplements clienteServiceImplements;

	@GetMapping("/cliente")
	public String index() {
		return "form-cliente";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
	@GetMapping("/editar-perfil/{id}")
	public String modificar(ModelMap modelo, HttpSession session, @PathVariable String id) {
		try {
			Cliente cliente = clienteServiceImplements.buscarClientePorId(id);
			modelo.put("cliente", cliente);

			// Para que el un usuario logueado no pueda editar el perfil de otro con el id
			Cliente login = (Cliente) session.getAttribute("usuariosession");
			if (login == null || !login.getId().equals(id)) {
				return "redirect:/inicio";
			}

		} catch (Exception e) {
			modelo.put("error", e.getMessage());
		}
		return "modificar-perfil";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
	@PostMapping("/editar-perfil/{id}")
	public String modificar(@RequestParam String id, HttpSession session, ModelMap modelo, MultipartFile archivo,
			@RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String telefono, @RequestParam String contraseña1, @RequestParam String contraseña2) {
		Cliente cliente = null;
		// Para que el un usuario logueado no pueda editar el perfil de otro con el id
		Cliente login = (Cliente) session.getAttribute("usuariosession");
		if (login == null || !login.getId().equals(id)) {
			return "redirect:/inicio";
		}
		
		try {

			clienteServiceImplements.modificar(archivo, id, documento, nombre, apellido, telefono, contraseña1,
					contraseña2);
			modelo.put("exito", "Modificacion exitosa");
			session.setAttribute("usuariosession", cliente);
			return "redirect:/inicio";
		} catch (ExceptionService e) {

			modelo.put("error", e.getMessage());
			return "redirect:/editar-perfil/{id}";
		}

	}
}
