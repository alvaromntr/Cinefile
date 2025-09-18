package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.RequestUsuario;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity getAllUsuarios() {
        var allUsuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(allUsuarios);
    }
    @PostMapping
    public ResponseEntity saveUsuario(@RequestBody  @Valid RequestUsuario data ) {
        Usuario newusuario = new Usuario(data);
        usuarioRepository.save(newusuario);
        return ResponseEntity.ok(newusuario);
    }
    @PutMapping
    @Transactional
    public ResponseEntity updaterUsuario(@RequestBody @Valid RequestUsuario data) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(data.id());
        if (optionalUsuario.isPresent()){
            Usuario usuario = optionalUsuario.get();
            usuario.setUsername(data.username());
            usuario.setEmail(data.email());
            usuario.setSenha_hash(data.senha_hash());
            return ResponseEntity.ok(usuario);
        }else {
            throw new EntityNotFoundException();
        }
    }
    @DeleteMapping("/{id}")//Define um endpoint DELETE que desativa um usuário pelo id
    public ResponseEntity deleteUsuario(@PathVariable Long id) {
         Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
         if (optionalUsuario.isPresent()){
             Usuario usuario = optionalUsuario.get();
             usuario.setActive(false);
             return ResponseEntity.noContent().build();
         }else  {
             throw new EntityNotFoundException();
         }
    }


    }


