package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "obras")

public class Obras_domain {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer obraId;
        // Titulo da obra não pode ser vazio ou nulo.
        @NotBlank
        private String titulo;
        private TipoObra_domain tipo;

        // getters e setters
        public Integer getObraId() {
            return obraId;
        }
        public void setObraId(Integer obraId) {
            this.obraId = obraId;
        }
        public String getTitulo() {
            return titulo;
        }
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
        public TipoObra_domain getTipo() {
            return tipo;
        }
        public void setTipo(TipoObra_domain tipo) {
            this.tipo = tipo;
        }
}

