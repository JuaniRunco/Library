package com.juani.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.juani.entity.Cliente;
import com.juani.entity.Foto;
import com.juani.enumeraciones.Rol;
import com.juani.exceptions.ExceptionService;
import com.juani.repository.ClienteRepository;

@Service
public class ClienteServiceImplements implements UserDetailsService {

	@Autowired
	private FotoServiceImplements fotoServiceImplements;

	@Autowired
	private ClienteRepository clienteRepository;

	@Transactional
	public void deleteById(String id) {
		clienteRepository.deleteById(id);
	}

	@Transactional
	public List<Cliente> todosLosClientes() {
		return clienteRepository.findAll();
	}

	@Transactional
	public void registrar(MultipartFile archivo, Long documento, String nombre, String apellido, String telefono,
			String contraseña1, String contraseña2) throws ExceptionService {

		// validar datos
		validar(archivo, documento, nombre, apellido, telefono, contraseña1, contraseña2);
		Cliente cliente = new Cliente();
		cliente.setDocumento(documento);
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setTelefono(telefono);
		cliente.setAlta(true);
		cliente.setRol(Rol.USUARIO);
		// enctriptamos la clave
		String encriptada = new BCryptPasswordEncoder().encode(contraseña1);
		cliente.setContraseña(encriptada);

		// Si el archivo contiene foto
		if (!archivo.isEmpty()) {
			String idFoto = null;
			// Se le agrega al idFoto la foto que ya tenia
			if (cliente.getFoto() != null) {
				idFoto = cliente.getFoto().getId();
			}
			// Caso contrario la actualiza
			Foto foto = fotoServiceImplements.actualizarFoto(idFoto, archivo);
			cliente.setFoto(foto);

			clienteRepository.save(cliente);
		}
	}

	@Transactional
	public void modificar(MultipartFile archivo, String id, Long documento, String nombre, String apellido,
			String telefono, String contraseña1, String contraseña2) throws ExceptionService {

		validar(archivo, documento, nombre, apellido, telefono, contraseña1, contraseña2);
		Optional<Cliente> respuesta = clienteRepository.findById(id);

		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente.setDocumento(documento);
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setTelefono(telefono);
			cliente.setAlta(true);

			// enctriptamos la clave
			String encriptada = new BCryptPasswordEncoder().encode(contraseña1);
			cliente.setContraseña(encriptada);
			// Si el archivo contiene foto
			if (!archivo.isEmpty()) {
				String idFoto = null;
				// Se le agrega al idFoto la foto que ya tenia
				if (cliente.getFoto() != null) {
					idFoto = cliente.getFoto().getId();
				}
				// Caso contrario la actualiza
				Foto foto = fotoServiceImplements.actualizarFoto(idFoto, archivo);
				cliente.setFoto(foto);
				clienteRepository.save(cliente);
			}
		} else {
			throw new ExceptionService("Error al modificar el cliente");
		}
	}

	@Transactional(readOnly = true)
	public void darBajaCliente(String id) throws ExceptionService {
		Optional<Cliente> respuesta = clienteRepository.findById(id);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente.setAlta(false);
			clienteRepository.save(cliente);
		} else {
			throw new ExceptionService("No se pudo dar de baja el cliente solicitado");
		}
	}

	@Transactional(readOnly = true)
	public void darAltaCliente(String id) throws ExceptionService {
		Optional<Cliente> respuesta = clienteRepository.findById(id);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente.setAlta(true);
			clienteRepository.save(cliente);
		} else {
			throw new ExceptionService("No se pudo dar de baja el cliente solicitada");
		}
	}

	@Transactional(readOnly = true)
	public List<Cliente> listarClientes() {
		List<Cliente> clientes = clienteRepository.listarCliente();
		return clientes;
	}

	@Transactional(readOnly = true)
	public Cliente buscarClientePorId(String id) {
		return clienteRepository.buscarPorId(id);
	}

	public void validar(MultipartFile archivo, Long documento, String nombre, String apellido, String telefono,
			String contraseña1, String contraseña2) throws ExceptionService {

		/*if (archivo == null || archivo.isEmpty()) {
			throw new ExceptionService("Falta cargar su foto.");
		}*/

		if (nombre.isEmpty() || nombre == null) {
			throw new ExceptionService("Falta el nombre.");

		}
		if (apellido == null || apellido.isEmpty()) {
			throw new ExceptionService("Falta el apellido.");

		}

		if (documento == null) {
			throw new ExceptionService("Falta el documento");

		}

		if (telefono == null || telefono.isEmpty()) {
			throw new ExceptionService("Falta el telefono");

		}

		if (contraseña1 == null || contraseña1.isEmpty() || contraseña1.length() < 6) {
			throw new ExceptionService("La contraseña debe superar los 6 digitos.");

		}

		if (contraseña2 == null || contraseña2.isEmpty()) {
			throw new ExceptionService("Falta repetir la contraseña");

		}

		if (!contraseña1.equals(contraseña2)) {
			throw new ExceptionService("Las contraseñas no son iguales");
		}

	}

	@Override
	public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
		Cliente cliente = clienteRepository.buscarPorNombre(nombre);
		if (cliente != null) {

			List<GrantedAuthority> permisos = new ArrayList<>();

			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + cliente.getRol());
			permisos.add(p1);

			// Esto me permite guardar el objeto usuario log, para luego ser utilizado
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuariosession", cliente);

			User user = new User(cliente.getNombre(), cliente.getContraseña(), permisos);
			return user;
		} else {
			return null;
		}
	}

}
