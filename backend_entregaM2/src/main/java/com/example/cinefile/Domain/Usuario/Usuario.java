package com.example.cinefile.Domain.Usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "usuario")
@Table(
        name = "usuario",
        indexes = {
                @Index(name = "idx_usuario_username", columnList = "username", unique = true),
                @Index(name = "idx_usuario_email", columnList = "email", unique = true)
        }
)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(unique = true, nullable = false, length = 120)
    private String email;

    @NotBlank
    @Size(min = 60, max = 100) // ex: BCrypt ~60 chars
    @Column(name = "senha_hash", nullable = false, length = 100)
    @JsonIgnore
    private String senhaHash;

    @Size(max = 255)
    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER;

    protected Usuario() {
        // JPA
    }

    public Usuario(String username, String email, String senhaHash, UserRole role) {
        this.username  = username;
        this.email     = email;
        this.senhaHash = senhaHash;
        this.active    = true;
        this.role      = (role == null ? UserRole.USER : role);
    }

    /* ===================== Normalização ===================== */
    @PrePersist
    @PreUpdate
    private void normalize() {
        if (username != null) username = username.trim().toLowerCase();
        if (email != null)    email    = email.trim().toLowerCase();
        if (role == null)     role     = UserRole.USER;
    }

    /* ===================== Getters/Setters ===================== */
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /** Spring Security lê a senha por aqui */
    @Override
    @JsonIgnore
    public String getPassword() { return senhaHash; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    /* ===================== UserDetails ===================== */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override @JsonIgnore public boolean isAccountNonExpired() { return true; }
    @Override @JsonIgnore public boolean isAccountNonLocked() { return true; }
    @Override @JsonIgnore public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }

    /* ===================== equals/hashCode ===================== */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /* ===================== toString (sem dados sensíveis) ===================== */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", role=" + role +
                '}';
    }
}
