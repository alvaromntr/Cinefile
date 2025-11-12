package com.example.cinefile.Service;

import com.example.cinefile.Domain.Usuario.UserRole;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!usuarioRepository.existsByUsername("admin")) {
            String senhaCriptografada = passwordEncoder.encode("admin123");

            Usuario admin = new Usuario(
                    "admin",
                    "admin@cinefile.com",
                    senhaCriptografada,
                    UserRole.ADMIN
            );

            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso!");
        }
    }
}