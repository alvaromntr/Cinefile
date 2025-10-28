package com.example.cinefile.Controller;


import com.example.cinefile.Service.UsuarioService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder encoder;

    public UsuarioController(UsuarioService usuarioService, BCryptPasswordEncoder encoder) {
        this.usuarioService = usuarioService;
        this.encoder = encoder;
    }
}
