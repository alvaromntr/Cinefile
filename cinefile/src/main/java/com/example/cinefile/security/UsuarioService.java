package com.example.cinefile.security;

import com.example.cinefile.Domain.Usuario.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final com.example.cinefile.Service.UsuarioService usuarioService;

    public UsuarioService(com.example.cinefile.Service.UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioService.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("Usuário não encontrado");
        return u;
    }
}
