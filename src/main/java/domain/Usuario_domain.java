package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "usuarios")

public class Usuario_domain {
    private long user_id;

    // REGRA DE NEGÓCIO 1:
    // O username não pode ser vazio nem conter apenas espaços.
    @NotBlank
    private String username;

    // REGRA DE NEGÓCIO 2:
    // A senha é obrigatória.
    @NotNull
    private String password;

    // REGRA DE NEGÓCIO 3:
    // O usuário deve possuir um e-mail válido para cadastro.
    @Email
    private String email;

    // REGRA DE NEGÓCIO 4:
    // Caso o usuário não envie uma foto de perfil, será atribuída uma padrão pelo sistema.
    private String profilePic;

}
