package com.example.cinefile.Domain.Usuario;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity(name = "usuario")
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha_hash;

    @Column
    private String foto_perfil;

    @Column(nullable = false)
    private boolean active = true;


    public Usuario(RequestUsuario data) {
        this.username = data.username();
        this.email = data.email();
        this.senha_hash = data.senha_hash();
        this.foto_perfil = data.foto_usuario();
        this.active = true;
    }


    public Usuario(String username, String email, String senha_hash) {
        this.username = username;
        this.email = email;
        this.senha_hash = senha_hash;
        this.active = true;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha_hash;  // Retorna senha hasheada
    }

    @Override
    public String getUsername() {
        return username;  // Retorna username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Conta não bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credenciais não expiram
    }

    @Override
    public boolean isEnabled() {
        return active;  // Usuário ativo
    }
}
