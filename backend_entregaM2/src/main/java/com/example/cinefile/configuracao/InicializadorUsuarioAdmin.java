package com.example.cinefile.configuracao;

import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InicializadorUsuarioAdmin implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder codificadorSenha;

    @Override
    public void run(String... args) throws Exception {
        if (!usuarioRepository.existsByUsername("admin")) {
            String senhaCriptografada = codificadorSenha.encode("admin123");

            Usuario admin = new Usuario(
                    "admin",
                    "admin@cinefile.com",
                    senhaCriptografada,
                    TipoUsuario.ADMINISTRADOR
            );

            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso!");
        }
    }
}