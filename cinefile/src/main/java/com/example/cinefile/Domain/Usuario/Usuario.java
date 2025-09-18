package com.example.cinefile.Domain.Usuario;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "usuario")
@Entity(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String Username;

    private String email;

    private String senha_hash;

    private String foto_perfil;

    private Boolean active;

    public Usuario(RequestUsuario requestUsuario){
        this.Username =requestUsuario.username();
        this.email=requestUsuario.email();
        this.senha_hash=requestUsuario.senha_hash();
        this.active=true;
    }
}
