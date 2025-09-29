package com.example.cinefile.ObraDomain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Obras")
@Table(name ="Obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID obra_id;

    private String titulo;

    private String descricao;

    private String tipo; // filme ou serie

    private Integer anolancamento;

    private String poster_url;

    private Integer duracao; // minutos, normalmente para filmes

    private Boolean active;

    public Obra(RequestObra requestObra) {
        this.titulo = requestObra.titulo();
        this.descricao = requestObra.descricao();
        this.tipo = requestObra.tipo();
        this.anolancamento = requestObra.ano_lancamento();
        this.poster_url = requestObra.poster_url();
        this.duracao = requestObra.duracao();
        this.active=true;
    }

    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getTipo() {
        return tipo;
    }
    public Integer getAno_lancamento() {
        return anolancamento;
    }
    public String getPoster_url() {
        return poster_url;
    }
    public Integer getDuracao() {
        return duracao;
    }
    public Boolean getActive() {
        return active;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setAno_lancamento(Integer ano_lancamento) {
        this.anolancamento = ano_lancamento;
    }
    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }
    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }
}
