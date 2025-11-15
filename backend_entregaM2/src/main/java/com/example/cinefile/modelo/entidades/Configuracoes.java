package com.example.cinefile.modelo.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracoes")
@Getter
@Setter
@NoArgsConstructor
public class Configuracoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean receberNotificacoesEmail = true;

    @Column(length = 20, nullable = false)
    private String temaInterface = "light";

    @Column(length = 10, nullable = false)
    private String idiomaPreferido = "pt-BR";

    public Configuracoes(Usuario usuario) {
        this.usuario = usuario;
    }

    public Configuracoes(Usuario usuario, Boolean receberNotificacoesEmail, String temaInterface, String idiomaPreferido) {
        this.usuario = usuario;
        this.receberNotificacoesEmail = receberNotificacoesEmail != null ? receberNotificacoesEmail : true;
        this.temaInterface = temaInterface != null ? temaInterface : "light";
        this.idiomaPreferido = idiomaPreferido != null ? idiomaPreferido : "pt-BR";
    }
}