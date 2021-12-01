package com.juani;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.juani.service.ClienteServiceImplements;

@SpringBootApplication
public class LibreriaApplication {
	
	@Autowired
	private ClienteServiceImplements clienteServiceImplements;
	
	public static void main(String[] args) {
		SpringApplication.run(LibreriaApplication.class, args);
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(clienteServiceImplements)
                .passwordEncoder(new BCryptPasswordEncoder());

    }
	
}
