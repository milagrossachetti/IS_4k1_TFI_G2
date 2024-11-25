package com.is.IS_4k1_TFI_G2.seguridad;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServicePersonalizado implements UserDetailsService {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public UserDetailsServicePersonalizado(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repositorioUsuario.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(usuario.getEmail(), usuario.getContrasenia(),
                Collections.singleton(new SimpleGrantedAuthority(usuario.getRol())));
    }
}
