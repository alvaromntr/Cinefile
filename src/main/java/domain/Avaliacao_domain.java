package domain;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao_domain {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private Integer avaliacaoId;

        private Integer usuarioId;

        private Integer obraId;

        @Column(precision = 3, scale = 1)
        private Double nota;
}
