package com.is.IS_4k1_TFI_G2.seguridad;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.UsuarioDato;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServicePersonalizado implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = UsuarioDato.USUARIOS.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(usuario.getEmail(), usuario.getContrasenia(),
                Collections.singleton(new SimpleGrantedAuthority(usuario.getRol())));
    }
}
